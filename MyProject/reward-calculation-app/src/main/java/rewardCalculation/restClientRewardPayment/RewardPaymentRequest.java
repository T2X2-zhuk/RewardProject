package rewardCalculation.restClientRewardPayment;

import lombok.*;
import rewardCalculation.dto.PaymentDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RewardPaymentRequest {

    private List<PaymentDTO> paymentDTOS;

}
