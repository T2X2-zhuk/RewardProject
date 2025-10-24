package rewardCalculation.transactionalOutBox.dispatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rewardCalculation.EnumObject.OutboxPaymentStatus;
import rewardCalculation.EnumObject.RewardStatus;
import rewardCalculation.restClientRewardPayment.RewardPaymentClient;
import rewardCalculation.transactionalOutBox.domain.OutboxPaymentEvent;
import rewardCalculation.transactionalOutBox.JPA.OutboxPaymentEventRepository;
import rewardCalculation.JPA.repositories.RewardRepository;
import rewardCalculation.dto.PaymentDTO;
import rewardCalculation.restClientRewardPayment.RewardPaymentResponse;
import rewardCalculation.lock.RewardExecutionLock;

import java.util.ArrayList;
import java.util.List;
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
@ConditionalOnProperty(
        prefix = "app",
        name = {"transactionalOutBox", "useAsyncOutboxDispatcher"},
        havingValue = "false"
)
class OutboxPaymentSyncDispatcher implements OutboxPaymentDispatcher{

    private final OutboxPaymentEventRepository outboxRepo;
    private final RewardRepository rewardRepository;
    private final RewardPaymentClient rewardPaymentClient;
    private final ObjectMapper objectMapper;
    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final RewardExecutionLock rewardExecutionLock;

    @Value( "${app.batch_size}" )
    private int batch_size;

    @Transactional
    @Scheduled(fixedDelay = 60000) // каждые 60 секунд
    @Timed(value = "outbox.dispatch.duration", description = "Время выполнения Outbox Dispatcher")
    @Counted(value = "outbox.dispatch.count", description = "Количество запусков Outbox Dispatcher")
    public void dispatchPendingPayments() {
        rewardExecutionLock.<Void>runWithLock("rewardCalculation", () -> {
            CircuitBreaker cb = circuitBreakerRegistry.circuitBreaker("rewardPaymentCb");

            if (cb.getState() == CircuitBreaker.State.OPEN) {
                log.warn("CircuitBreaker is OPEN. Skipping dispatch cycle.");
                return null;
            }
            // Берём все PENDING и FAILED события
            List<OutboxPaymentEvent> pendingEvents = outboxRepo.findByStatusesReadyForProcessing(
                    List.of(OutboxPaymentStatus.PENDING, OutboxPaymentStatus.FAILED),
                    Pageable.ofSize(batch_size)
            );

            for (OutboxPaymentEvent event : pendingEvents) {
                if (cb.getState() == CircuitBreaker.State.OPEN) {
                    List<Long> remainingIds = getLongs(event, pendingEvents);

                    if (!remainingIds.isEmpty()) {
                        outboxRepo.markEventsAsFailed(remainingIds);
                    }
                    break;
                }

                try {
                    // Атомарное переводим в PROCESSING
                    int updated = outboxRepo.updateStatusIfPendingOrFailed(event.getId(), OutboxPaymentStatus.PROCESSING);
                    if (updated == 0) continue;

                    // Попытка отправки платежей
                    PaymentDTO[] payments = objectMapper.readValue(event.getPayload(), PaymentDTO[].class);
                    RewardPaymentResponse response = rewardPaymentClient.payReward(List.of(payments));

                    if (!response.isSuccessfulSaving()) {
                        List<Long> remainingIds = getLongs(event, pendingEvents);
                        if (!remainingIds.isEmpty()) {
                            outboxRepo.markEventsAsFailed(remainingIds);
                        }
                        break;
                    }

                    // Успешная отправка
                    if (event.getRewardIds() != null && !event.getRewardIds().isEmpty()) {
                        rewardRepository.rewardSetStatusForList(RewardStatus.PAID, event.getRewardIds());
                    }

                    // Удаляем событие из Outbox
                    outboxRepo.delete(event);

                } catch (Exception e) {
                    // Любая ошибка → сразу в FAILED
                    markEventFailed(event);
                }
             }

            log.info("Обработано {} событий из Outbox (batch size = {})", pendingEvents.size(), batch_size);
            return null;
        });
    }

    @NotNull
    private static List<Long> getLongs(OutboxPaymentEvent event, List<OutboxPaymentEvent> pendingEvents) {
        List<Long> remainingIds = new ArrayList<>();
        boolean startCollecting = false;

        for (OutboxPaymentEvent e : pendingEvents) {
            if (startCollecting) {
                remainingIds.add(e.getId());
            }
            if (e.equals(event)) { // текущий элемент — точка остановки
                startCollecting = true;
                remainingIds.add(e.getId());
            }
        }
        return remainingIds;
    }

    private void markEventFailed(OutboxPaymentEvent event) {
        outboxRepo.markAsFailed(event.getId());
    }
}
