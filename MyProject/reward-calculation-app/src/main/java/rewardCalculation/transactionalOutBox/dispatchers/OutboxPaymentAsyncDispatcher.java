package rewardCalculation.transactionalOutBox.dispatchers;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import rewardCalculation.EnumObject.OutboxPaymentStatus;
import rewardCalculation.EnumObject.RewardStatus;
import rewardCalculation.JPA.repositories.RewardRepository;
import rewardCalculation.dto.PaymentDTO;
import rewardCalculation.lock.RewardExecutionLock;
import rewardCalculation.restClientRewardPayment.RewardPaymentClient;
import rewardCalculation.restClientRewardPayment.RewardPaymentResponse;
import rewardCalculation.transactionalOutBox.JPA.OutboxPaymentEventRepository;
import rewardCalculation.transactionalOutBox.domain.OutboxPaymentEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

//Класс который:
//Берет OutboxPaymentEvent из Outbox  со статусами PENDING или FAILED.
//Переводит его в статус PROCESSING (атомарно, через SQL update).
//Отправляют платежи через rewardPaymentClient.payReward.
//Если успешно, обновляют статусы наград (RewardStatus.PAID).
//Если неудачно — помечают событие как FAILED.
//Удаляют обработанное событие из Outbox.
@Service
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
@ConditionalOnExpression("${app.transactionalOutBox:true} == true and ${app.useAsyncOutboxDispatcher:true} == true")
 class OutboxPaymentAsyncDispatcher implements OutboxPaymentDispatcher{

    private final OutboxPaymentEventRepository outboxRepo;
    private final RewardRepository rewardRepository;
    private final RewardPaymentClient rewardPaymentClient;
    private final ObjectMapper objectMapper;
    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final RewardExecutionLock rewardExecutionLock;
    private final Executor outboxTaskExecutor;
    private final ApplicationContext applicationContext; // для вызова @Transactional метода через Spring
    private final TransactionTemplate transactionTemplate;

    @Value("${app.batch_size}")
    private int batch_size;

    @Scheduled(fixedDelay = 60000)
    public void dispatchPendingPayments() {
        rewardExecutionLock.<Void>runWithLock("rewardCalculation", () -> {

            CircuitBreaker cb = circuitBreakerRegistry.circuitBreaker("rewardPaymentCb");
            if (cb.getState() == CircuitBreaker.State.OPEN) {
                log.warn("CircuitBreaker is OPEN. Skipping dispatch cycle.");
                return null;
            }

            //Загружаем события с rewardIds в транзакции
            List<OutboxPaymentEvent> pendingEvents = fetchPendingEvents(batch_size);

            List<CompletableFuture<Void>> futures = new ArrayList<>();
            for (OutboxPaymentEvent event : pendingEvents) {

                //Копируем rewardIds
                List<Long> rewardIds = new ArrayList<>(event.getRewardIds());

                //Асинхронный вызов
                OutboxPaymentAsyncDispatcher self = applicationContext.getBean(OutboxPaymentAsyncDispatcher.class);
                CompletableFuture<Void> future = CompletableFuture.runAsync(
                        () -> self.processEventTransactional(event.getId(), rewardIds),
                        outboxTaskExecutor
                );

                futures.add(future);
            }

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            log.info("Обработано {} событий из Outbox (batch size = {})", pendingEvents.size(), batch_size);
            return null;
        });
    }

    public List<OutboxPaymentEvent> fetchPendingEvents(int batchSize) {
        return transactionTemplate.execute(status -> {
            List<OutboxPaymentEvent> events = outboxRepo.findByStatusesReadyForProcessing(
                    List.of(OutboxPaymentStatus.PENDING, OutboxPaymentStatus.FAILED),
                    Pageable.ofSize(batchSize)
            );
            events.forEach(e -> Hibernate.initialize(e.getRewardIds()));
            return events;
        });
    }
    @Transactional
    public void processEventTransactional(Long eventId, List<Long> rewardIds) {
        OutboxPaymentEvent event = outboxRepo.findById(eventId).orElse(null);
        if (event == null) return;

        int updated = outboxRepo.updateStatusIfPendingOrFailed(eventId, OutboxPaymentStatus.PROCESSING);
        if (updated == 0) return;

        CircuitBreaker cb = circuitBreakerRegistry.circuitBreaker("rewardPaymentCb");
        if (cb.getState() == CircuitBreaker.State.OPEN) {
            outboxRepo.markAsFailed(eventId);
            return;
        }

        try {
            PaymentDTO[] payments = objectMapper.readValue(event.getPayload(), PaymentDTO[].class);
            RewardPaymentResponse response = rewardPaymentClient.payReward(List.of(payments));

            if (response.isSuccessfulSaving()) {
                if (!rewardIds.isEmpty()) {
                    rewardRepository.rewardSetStatusForList(RewardStatus.PAID, rewardIds);
                }
                outboxRepo.delete(event);
            } else {
                outboxRepo.markAsFailed(eventId);
            }
            log.debug("Event {} processed successfully", eventId);
        } catch (Exception e) {
            outboxRepo.markAsFailed(eventId);
            log.error("Failed to process event {}: {}", eventId, e.getMessage());
        }
    }
}
