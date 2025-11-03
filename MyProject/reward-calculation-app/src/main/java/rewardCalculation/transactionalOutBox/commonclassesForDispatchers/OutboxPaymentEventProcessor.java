package rewardCalculation.transactionalOutBox.commonclassesForDispatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import rewardCalculation.transactionalOutBox.jpa.enums.OutboxPaymentStatus;
import rewardCalculation.jpa.enums.RewardStatus;
import rewardCalculation.jpa.repositories.RewardRepository;
import rewardCalculation.dto.PaymentDTO;
import rewardCalculation.restClientRewardPayment.RewardPaymentClient;
import rewardCalculation.restClientRewardPayment.RewardPaymentResponse;
import rewardCalculation.transactionalOutBox.jpa.repositories.OutboxPaymentEventRepository;
import rewardCalculation.transactionalOutBox.jpa.domain.OutboxPaymentEvent;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnExpression("${app.transactionalOutBox:true} == true")
public class OutboxPaymentEventProcessor {

    private final OutboxPaymentEventRepository outboxRepo;
    private final RewardRepository rewardRepository;
    private final RewardPaymentClient rewardPaymentClient;
    private final ObjectMapper objectMapper;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    @Transactional
    public void processEvent(OutboxPaymentEvent event) {
        if (event == null) return;

        int updated = outboxRepo.updateStatusIfPendingOrFailed(
                event.getId(),
                OutboxPaymentStatus.PROCESSING,
                List.of(OutboxPaymentStatus.PENDING, OutboxPaymentStatus.FAILED)
        );
        if (updated == 0) {
            log.info("Event {} is already being processed by another worker", event.getId());
            return;
        }

        if (getCircuitBreakerState() == CircuitBreaker.State.OPEN) {
            outboxRepo.markAsFailed(event.getId());
            return;
        }

        try {
            log.info("Start processing Outbox event {} with rewards {}", event.getId(), event.getRewardIds());

            PaymentDTO[] payments = objectMapper.readValue(event.getPayload(), PaymentDTO[].class);

            log.debug("Payload deserialized: {}", Arrays.toString(payments));

            RewardPaymentResponse response = rewardPaymentClient.payReward(List.of(payments));
            log.info("Payment response for event {}: {}", event.getId(), response);

            if (response.isSuccessfulSaving()) {
                if (event.getRewardIds() != null && !event.getRewardIds().isEmpty()) {
                    rewardRepository.rewardSetStatusForList(RewardStatus.PAID, event.getRewardIds());
                }
                outboxRepo.delete(event);
                log.info("Event {} processed successfully and removed from Outbox", event.getId());
            } else {
                outboxRepo.markAsFailed(event.getId());
                log.warn("Event {} failed during payment processing", event.getId());
            }

            log.debug("Event {} processed successfully", event.getId());

        } catch (Exception e) {
            outboxRepo.markAsFailed(event.getId());
            log.error("Failed to process event {}: {}", event.getId(), e.getMessage());
        }
    }

    public CircuitBreaker.State getCircuitBreakerState() {
        return circuitBreakerRegistry.circuitBreaker("rewardPaymentCb").getState();
    }
}
