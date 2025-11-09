package rewardCalculation.services.reward;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import rewardCalculation.dto.PaymentDTO;
import rewardCalculation.jpa.enums.RewardStatus;
import rewardCalculation.jpa.domain.Reward;
import rewardCalculation.jpa.repositories.RewardRepository;
import rewardCalculation.requests.PaymentBatchMessage;
import rewardCalculation.restClientRewardPayment.RewardPaymentResponse;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(
        prefix = "app",
        name = "transactionalOutBox",
        havingValue = "false"
)
public class UpdatingRewardsDependingOnTheResultReceivedFromAnExternalService {

    private final RewardRepository rewardRepository;

    private final KafkaTemplate<String, PaymentBatchMessage> kafkaTemplate;

    private static final String PAYMENT_TOPIC = "reward-payments";

    @Transactional
    public RewardPaymentResponse result(RewardPaymentResponse response,List<Reward> rewards,List<PaymentDTO> paymentDTOS){
        log.info("{} is start!",this.getClass().getSimpleName());

        if (!response.isSuccessfulSaving() || response.hasErrors()){
            sendMessageToKafka(rewards, paymentDTOS);
        }else {
            List<Long> rewardIds = rewards.stream().map(Reward::getId).collect(Collectors.toList());

            rewardRepository.rewardSetStatusForList(RewardStatus.PAID,rewardIds);
            log.info("All is OK");
        }

        return response;
    }

    private void sendMessageToKafka(List<Reward> rewards, List<PaymentDTO> paymentDTOS) {
        log.warn("Payment service unavailable or failed — sending to Kafka fallback topic...");
        try {
            log.info("Payments sent to Kafka for deferred processing: {}", paymentDTOS);

            PaymentBatchMessage paymentBatchMessage = new PaymentBatchMessage(paymentDTOS);
            kafkaTemplate.send(PAYMENT_TOPIC, paymentBatchMessage);

            List<Long> rewardIds = rewards.stream().map(Reward::getId).collect(Collectors.toList());
            rewardRepository.rewardSetStatusForList(RewardStatus.PROCESSING, rewardIds);

        } catch (Exception e) {
            log.error("Failed to send rewards to Kafka", e);
        }
    }
}
