package rewardCalculation.restClientRewardPayment;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import rewardCalculation.dto.PaymentDTO;

import java.util.List;

@Component
@Slf4j
public class RewardPaymentClient {


    private final RestTemplate restTemplate;
    private final String baseUrl;

    public RewardPaymentClient(String baseUrl, RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    @Retry(name = "rewardPaymentRetry")
    public RewardPaymentResponse payReward(List<PaymentDTO> payments) {
        log.info("Calling reward-payment-app at {}", baseUrl);
        RewardPaymentRequest request = new RewardPaymentRequest(payments);

        RewardPaymentResponse response = restTemplate.postForObject(
                baseUrl + "/reward/payment/payReward",
                request,
                RewardPaymentResponse.class
        );

        log.info("Reward-payment-app call succeeded!");
        return response;
    }
}
