package rewardPayment.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import rewardPayment.requests.PaymentBatchMessage;
import rewardPayment.services.RewardPaymentService;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RewardPaymentKafkaConsumer {

    private final RewardPaymentService rewardPaymentService;
    private final KafkaTemplate<String, PaymentBatchMessage> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String RESULT_TOPIC = "reward-payment-results";


    @KafkaListener(topics = "reward-payments", groupId = "reward-payment-group")
    public void consume(Map<String, Object> messageMap, Acknowledgment ack) {
        try {

            PaymentBatchMessage message = objectMapper.convertValue(messageMap, PaymentBatchMessage.class);

            log.info("Получено сообщение: {}", message);

            rewardPaymentService.pay(message)
                    .doOnSuccess(r -> {
                        ack.acknowledge();
                        kafkaTemplate.send(RESULT_TOPIC, message);
                        log.info("PaymentBatchMessage отправлен обратно в Kafka: {}", message);
                    })
                    .doOnError(e -> log.error("Ошибка при обработке платежа", e))
                    .subscribe();

        } catch (Exception e) {
            log.error("Ошибка конвертации сообщения", e);
        }
    }
}
