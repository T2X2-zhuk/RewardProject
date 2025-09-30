package rewardCalculation.calculate;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.restClientRewardPayment.RewardPaymentClient;
import rewardCalculation.dto.PaymentDTO;
import rewardCalculation.restClientRewardPayment.RewardPaymentRequest;
import rewardCalculation.restClientRewardPayment.RewardPaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@ToString
@Slf4j
public class SendPaymentsToMicroservice {

    private final RewardPaymentClient paymentClient;

    public RewardPaymentResponse send(List<PaymentDTO> paymentDTOS) {
        log.info("{} start!", this);
        RewardPaymentRequest request = new RewardPaymentRequest(paymentDTOS);
       log.debug("PaymentDTOS sent!");
        RewardPaymentResponse response = paymentClient.payReward(request.getPaymentDTOS());
        log.debug("Response : {}",response);
        log.info("{} execute!" , this);
        return response;
    }
}
