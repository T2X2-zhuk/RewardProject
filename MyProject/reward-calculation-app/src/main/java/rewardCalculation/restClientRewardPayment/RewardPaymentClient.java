package rewardCalculation.restClientRewardPayment;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.dto.PaymentDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@Slf4j
public class RewardPaymentClient {

    private final RestClient rewardPaymentRestClient;

    public RewardPaymentClient(RestClient rewardPaymentRestClient) {
        this.rewardPaymentRestClient = rewardPaymentRestClient;
    }

    public RewardPaymentResponse payReward(List<PaymentDTO> paymentDTOS) {
        log.info("{} is start!",this.getClass().getSimpleName());
        RewardPaymentRequest request = new RewardPaymentRequest(paymentDTOS);
        RewardPaymentResponse response = rewardPaymentRestClient.post()
                .uri("/reward/payment/payReward")
                .body(request)
                .retrieve()
                .body(RewardPaymentResponse.class);
        log.info("{} is execute!",this.getClass().getSimpleName());
        return response;
    }

}
