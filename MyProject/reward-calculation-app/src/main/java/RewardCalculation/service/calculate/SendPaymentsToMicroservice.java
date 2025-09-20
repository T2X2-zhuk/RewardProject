package RewardCalculation.service.calculate;

import RewardCalculation.dto.PaymentDTO;
import RewardCalculation.restClientRewardPayment.RewardPaymentClient;
import RewardCalculation.restClientRewardPayment.RewardPaymentRequest;
import RewardCalculation.restClientRewardPayment.RewardPaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SendPaymentsToMicroservice {

    @Autowired
    private RewardPaymentClient paymentClient;

    public RewardPaymentResponse send(List<PaymentDTO> paymentDTOS) {
        RewardPaymentRequest request = new RewardPaymentRequest(paymentDTOS);
        System.out.println("Платеж отправлен!");
        return paymentClient.payReward(request.getPaymentDTOS());
    }
}
