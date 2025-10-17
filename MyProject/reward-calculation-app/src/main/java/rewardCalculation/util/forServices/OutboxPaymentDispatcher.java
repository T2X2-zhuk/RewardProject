package rewardCalculation.util.forServices;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxPaymentDispatcher {

    private final OutboxPaymentEventRepository outboxRepo;
    private final RewardRepository rewardRepository;
    private final SendPaymentsToRewardPaymentApplication rewardPaymentClient;
    private final ObjectMapper objectMapper;

    private static final int BATCH_SIZE = 20;

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void dispatchPendingPayments() {
        List<OutboxPaymentEvent> pendingEvents = outboxRepo.findTopNByStatusesReadyForProcessing(
                List.of("PENDING", "FAILED"),
                LocalDateTime.now(),
                Pageable.ofSize(BATCH_SIZE)
        );

        for (OutboxPaymentEvent event : pendingEvents) {
            try {
                processEvent(event);
            } catch (Exception e) {
                log.warn("Event {} failed: {}, will retry later", event.getId(), e.getMessage());
                event.setStatus("FAILED");
                event.setNextAttemptAt(LocalDateTime.now().plusSeconds(5)); // повтор через 5 сек
                outboxRepo.save(event);
            }
        }
    }

    @Transactional
    public void processEvent(OutboxPaymentEvent event) throws Exception {
        int updated = outboxRepo.updateStatusIfPendingOrFailed(event.getId(), "PROCESSING");
        if (updated == 0) return;

        PaymentDTO[] payments = objectMapper.readValue(event.getPayload(), PaymentDTO[].class);

        RewardPaymentResponse response = rewardPaymentClient.send(List.of(payments));

        if (!response.isSuccessfulSaving()) {
            throw new RuntimeException("Payment service unavailable, Dispatcher will retry");
        }

        // Обновляем Rewards только после успешной доставки
        if (event.getRewardIds() != null && !event.getRewardIds().isEmpty()) {
            rewardRepository.rewardSetStatusForList(RewardStatus.PAID, event.getRewardIds());
        }

        // Удаляем событие из Outbox
        outboxRepo.delete(event);
    }
}
