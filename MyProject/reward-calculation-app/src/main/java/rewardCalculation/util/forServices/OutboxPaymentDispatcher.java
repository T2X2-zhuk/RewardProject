package rewardCalculation.util.forServices;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rewardCalculation.JPA.domain.EnumObject.RewardStatus;
import rewardCalculation.JPA.domain.OutboxPaymentEvent;
import rewardCalculation.JPA.repositories.OutboxPaymentEventRepository;
import rewardCalculation.JPA.repositories.RewardRepository;
import rewardCalculation.calculate.SendPaymentsToRewardPaymentApplication;
import rewardCalculation.dto.PaymentDTO;
import rewardCalculation.restClientRewardPayment.RewardPaymentResponse;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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
        // Берём все PENDING и FAILED события
        List<OutboxPaymentEvent> pendingEvents = outboxRepo.findTopNByStatusesReadyForProcessing(
                List.of("PENDING", "FAILED"),
                Pageable.ofSize(BATCH_SIZE)
        );

        for (OutboxPaymentEvent event : pendingEvents) {
            if (cb.getState() == CircuitBreaker.State.OPEN) {
                log.warn("CB OPEN, пропускаем отправку события {}", event.getId());
                continue; // оставляем событие в Outbox
            }
            try {
                // Атомарное переводим в PROCESSING
                int updated = outboxRepo.updateStatusIfPendingOrFailed(event.getId(), "PROCESSING");
                if (updated == 0) continue;

                // Попытка отправки платежей
                PaymentDTO[] payments = objectMapper.readValue(event.getPayload(), PaymentDTO[].class);
                RewardPaymentResponse response = rewardPaymentClient.send(List.of(payments));

                if (!response.isSuccessfulSaving()) {
                    markEventFailed(event); // сразу в FAILED
                    continue;
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

    private void markEventFailed(OutboxPaymentEvent event) {
        outboxRepo.markAsFailed(event.getId());
    }
}
