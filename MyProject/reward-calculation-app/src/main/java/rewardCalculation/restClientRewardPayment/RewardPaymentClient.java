package rewardCalculation.restClientRewardPayment;

import lombok.RequiredArgsConstructor;
import rewardCalculation.dto.PaymentDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class RewardPaymentClient {

    private final RestClient rewardPaymentRestClient;

    public RewardPaymentClient(RestClient rewardPaymentRestClient) {
        this.rewardPaymentRestClient = rewardPaymentRestClient;
    }

    public RewardPaymentResponse payReward(List<PaymentDTO> paymentDTOS) {
        RewardPaymentRequest request = new RewardPaymentRequest(paymentDTOS);
        return rewardPaymentRestClient.post()
                .uri("/reward/payment/payReward")
                .body(request)
                .retrieve()
                .body(RewardPaymentResponse.class);
    }

}
