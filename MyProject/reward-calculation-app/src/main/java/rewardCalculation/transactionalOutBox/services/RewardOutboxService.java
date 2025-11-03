package rewardCalculation.transactionalOutBox.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import rewardCalculation.transactionalOutBox.jpa.enums.OutboxPaymentStatus;
import rewardCalculation.jpa.enums.RewardStatus;
import rewardCalculation.transactionalOutBox.jpa.domain.OutboxPaymentEvent;
import rewardCalculation.jpa.domain.Reward;
import rewardCalculation.transactionalOutBox.jpa.repositories.OutboxPaymentEventRepository;
import rewardCalculation.jpa.repositories.RewardRepository;
import rewardCalculation.dto.PaymentDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnExpression("${app.transactionalOutBox:true} == true")
public class RewardOutboxService {

    private final OutboxPaymentEventRepository outboxRepo;
    private final RewardRepository rewardRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void schedulePayments(List<PaymentDTO> payments, List<Reward> rewards) {
        try {
            List<Long> rewardIds = rewards.stream().map(Reward::getId).collect(Collectors.toList());

            outboxRepo.save(OutboxPaymentEvent.builder()
                    .rewardIds(rewardIds)
                    .payload(objectMapper.writeValueAsString(payments))
                    .status(OutboxPaymentStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .build());

            // Rewards переводим в PROCESSING
            rewardRepository.rewardSetStatusForList(RewardStatus.PROCESSING, rewardIds);

            log.info("✅ Created OutboxPaymentEvent for rewards {}", rewardIds);

        } catch (Exception e) {

            log.error("Failed to schedule payments: {}", e.getMessage());
            throw new RuntimeException("Failed to schedule payments", e);

        }
    }
}
