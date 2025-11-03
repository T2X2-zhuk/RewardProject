package rewardCalculation.transactionalOutBox.dispatchers;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rewardCalculation.transactionalOutBox.commonclassesForDispatchers.OutboxPaymentDispatcher;
import rewardCalculation.transactionalOutBox.commonclassesForDispatchers.OutboxPaymentEventProcessor;
import rewardCalculation.transactionalOutBox.jpa.enums.OutboxPaymentStatus;
import rewardCalculation.transactionalOutBox.jpa.domain.OutboxPaymentEvent;
import rewardCalculation.transactionalOutBox.jpa.repositories.OutboxPaymentEventRepository;
import rewardCalculation.lock.RewardExecutionLock;

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
@ConditionalOnExpression("${app.transactionalOutBox:true} == true and ${app.useAsyncOutboxDispatcher:false} == false")
class OutboxPaymentSyncDispatcher implements OutboxPaymentDispatcher {

    private final OutboxPaymentEventRepository outboxRepo;
    private final OutboxPaymentEventProcessor eventProcessor;
    private final RewardExecutionLock rewardExecutionLock;

    @Value( "${app.batch_size}" )
    private int batch_size;

    @Scheduled(fixedDelay = 60000) // каждые 60 секунд
    @Timed(value = "outbox.dispatch.duration", description = "Время выполнения Outbox Dispatcher")
    @Counted(value = "outbox.dispatch.count", description = "Количество запусков Outbox Dispatcher")
    public void dispatchPendingPayments() {
        rewardExecutionLock.<Void>runWithLock("rewardCalculation", () -> {
            // Загружаем batch
            List<OutboxPaymentEvent> pendingEvents = outboxRepo.findByStatusesReadyForProcessing(
                    List.of(OutboxPaymentStatus.PENDING, OutboxPaymentStatus.FAILED),
                    Pageable.ofSize(batch_size)
            );

            if (pendingEvents.isEmpty()) return null;

            if (eventProcessor.getCircuitBreakerState() == CircuitBreaker.State.OPEN) {
                List<Long> ids = pendingEvents.stream().map(OutboxPaymentEvent::getId).toList();
                outboxRepo.markEventsAsFailed(ids);
                log.warn("CircuitBreaker OPEN. Marked {} events as FAILED", ids.size());
                return null;
            }

            for (OutboxPaymentEvent event : pendingEvents) {
                eventProcessor.processEvent(event);
            }

            log.info("Обработано {} событий из Outbox (batch size = {})", pendingEvents.size(), batch_size);
            return null;
        });
    }
}
