package rewardCalculation.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import rewardCalculation.dto.PaymentDTO;
import rewardCalculation.jpa.enums.RewardStatus;
import rewardCalculation.jpa.repositories.RewardRepository;
import rewardCalculation.requests.PaymentBatchMessage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(
        prefix = "app",
        name = "transactionalOutBox",
        havingValue = "false"
)
public class RewardPaymentResultConsumer {

    private final RewardRepository rewardRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "reward-payment-results", groupId = "reward-calculation-group")
    public void consume(Map<String, Object> messageMap,Acknowledgment ack) {
        try {
            PaymentBatchMessage message = objectMapper.convertValue(messageMap, PaymentBatchMessage.class);
            log.info("Получено подтверждение платежей: {}", message);

            List<Long> rewardIds = message.getPaymentDTOS().stream()
                    .map(PaymentDTO::getRewardId)
                    .collect(Collectors.toList());

            rewardRepository.rewardSetStatusForList(RewardStatus.PAID, rewardIds);
            ack.acknowledge();
            log.info("Статусы Reward обновлены для всех платежей из PaymentBatchMessage");
        } catch (IllegalArgumentException e) {
            log.error("Ошибка при конвертации сообщения из Map в PaymentBatchMessage", e);
        }
    }
}
