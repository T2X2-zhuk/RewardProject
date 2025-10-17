package rewardCalculation.util.forServices;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rewardCalculation.JPA.domain.EnumObject.RewardStatus;
import rewardCalculation.JPA.domain.OutboxPaymentEvent;
import rewardCalculation.JPA.domain.Reward;
import rewardCalculation.JPA.repositories.OutboxPaymentEventRepository;
import rewardCalculation.JPA.repositories.RewardRepository;
import rewardCalculation.dto.PaymentDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RewardOutboxService {

    private final OutboxPaymentEventRepository outboxRepo;
    private final RewardRepository rewardRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void schedulePayments(List<PaymentDTO> payments, List<Reward> rewards) {
        try {
            List<Long> rewardIds = rewards.stream().map(Reward::getId).collect(Collectors.toList());
            String payload = objectMapper.writeValueAsString(payments);

            OutboxPaymentEvent event = OutboxPaymentEvent.builder()
                    .rewardIds(rewardIds)
                    .payload(payload)
                    .status("PENDING")
                    .createdAt(LocalDateTime.now())
                    .build();

            outboxRepo.save(event);

            // Rewards переводим в PROCESSING
            rewardRepository.rewardSetStatusForList(RewardStatus.PROCESSING, rewardIds);

            log.info("✅ Created OutboxPaymentEvent for rewards {}", rewardIds);

        } catch (Exception e) {
            log.error("Failed to schedule payments: {}", e.getMessage());
            throw new RuntimeException("Failed to schedule payments", e);
        }
    }
}
