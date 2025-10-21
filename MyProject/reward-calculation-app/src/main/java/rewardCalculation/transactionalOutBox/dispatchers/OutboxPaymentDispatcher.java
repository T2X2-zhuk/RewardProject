package rewardCalculation.transactionalOutBox.dispatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rewardCalculation.EnumObject.OutboxPaymentStatus;
import rewardCalculation.EnumObject.RewardStatus;
import rewardCalculation.transactionalOutBox.domain.OutboxPaymentEvent;
import rewardCalculation.transactionalOutBox.JPA.OutboxPaymentEventRepository;
import rewardCalculation.JPA.repositories.RewardRepository;
import rewardCalculation.calculate.SendPaymentsToRewardPaymentApplication;
import rewardCalculation.dto.PaymentDTO;
import rewardCalculation.restClientRewardPayment.RewardPaymentResponse;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(
        prefix = "app",
        name = "transactionalOutBox",
        havingValue = "true"
)
public class OutboxPaymentDispatcher {

    private final OutboxPaymentEventRepository outboxRepo;
    private final RewardRepository rewardRepository;
    private final SendPaymentsToRewardPaymentApplication rewardPaymentClient;
    private final ObjectMapper objectMapper;
    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private static final int BATCH_SIZE = 20;

    @Transactional
    @Scheduled(fixedDelay = 60000) // каждые 60 секунд
    public void dispatchPendingPayments() {
        CircuitBreaker cb = circuitBreakerRegistry.circuitBreaker("rewardPaymentCb");

        if (cb.getState() == CircuitBreaker.State.OPEN) {
            log.warn("CircuitBreaker is OPEN. Skipping dispatch cycle.");
            return;
        }
        // Берём все PENDING и FAILED события
        List<OutboxPaymentEvent> pendingEvents = outboxRepo.findTopNByStatusesReadyForProcessing(
                List.of(OutboxPaymentStatus.PENDING, OutboxPaymentStatus.FAILED),
                Pageable.ofSize(BATCH_SIZE)
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
                RewardPaymentResponse response = rewardPaymentClient.send(List.of(payments));

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
