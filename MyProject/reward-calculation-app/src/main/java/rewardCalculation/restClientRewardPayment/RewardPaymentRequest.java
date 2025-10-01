package rewardCalculation.restClientRewardPayment;

import lombok.*;
import rewardCalculation.dto.PaymentDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RewardPaymentRequest {

    private List<PaymentDTO> paymentDTOS;

}
