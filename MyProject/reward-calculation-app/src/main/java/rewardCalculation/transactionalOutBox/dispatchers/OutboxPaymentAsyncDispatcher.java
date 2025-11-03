package rewardCalculation.transactionalOutBox.dispatchers;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import rewardCalculation.transactionalOutBox.commonclassesForDispatchers.OutboxPaymentDispatcher;
import rewardCalculation.transactionalOutBox.commonclassesForDispatchers.OutboxPaymentEventProcessor;
import rewardCalculation.transactionalOutBox.jpa.enums.OutboxPaymentStatus;
import rewardCalculation.lock.RewardExecutionLock;
import rewardCalculation.transactionalOutBox.jpa.repositories.OutboxPaymentEventRepository;
import rewardCalculation.transactionalOutBox.jpa.domain.OutboxPaymentEvent;

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
 class OutboxPaymentAsyncDispatcher implements OutboxPaymentDispatcher {

    private final OutboxPaymentEventRepository outboxRepo;
    private final OutboxPaymentEventProcessor eventProcessor;
    private final RewardExecutionLock rewardExecutionLock;
    private final Executor outboxTaskExecutor;
    private final TransactionTemplate transactionTemplate;

    @Value("${app.batch_size}")
    private int batch_size;

    @Scheduled(fixedDelay = 60000)
    @Timed(value = "outbox.dispatch.duration", description = "Время выполнения Outbox Dispatcher")
    @Counted(value = "outbox.dispatch.count", description = "Количество запусков Outbox Dispatcher")
    public void dispatchPendingPayments() {
        rewardExecutionLock.<Void>runWithLock("rewardCalculation", () -> {

            List<OutboxPaymentEvent> pendingEvents = outboxRepo.findByStatusesReadyForProcessing(
                    List.of(OutboxPaymentStatus.PENDING, OutboxPaymentStatus.FAILED),
                    org.springframework.data.domain.Pageable.ofSize(batch_size)
            );

            if (pendingEvents.isEmpty()) return null;

            if (eventProcessor.getCircuitBreakerState() == CircuitBreaker.State.OPEN) {
                List<Long> ids = pendingEvents.stream().map(OutboxPaymentEvent::getId).toList();
                outboxRepo.markEventsAsFailed(ids);
                log.warn("CircuitBreaker OPEN. Marked {} events as FAILED", ids.size());
                return null;
            }

            // Асинхронная обработка с явной транзакцией
            List<CompletableFuture<Void>> futures = pendingEvents.stream()
                    .map(event -> CompletableFuture.runAsync(() -> {
                        try {
                            transactionTemplate.execute(status -> {
                                log.info("Start processing Outbox event {} with rewards {}", event.getId(), event.getRewardIds());
                                eventProcessor.processEvent(event);
                                return null;
                            });
                        } catch (Exception e) {
                            log.error("Exception processing event {}: {}", event.getId(), e.getMessage(), e);
                        }
                    }, outboxTaskExecutor))
                    .toList();
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            log.info("Обработано {} событий из Outbox (batch size = {})", pendingEvents.size(), batch_size);
            return null;
        });
    }
}
