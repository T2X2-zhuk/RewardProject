package RewardPayment.requests;

import RewardPayment.dto.PaymentDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CommonRequestForPaymentParameters {

    private PaymentDTO paymentDTO;
    private List<PaymentDTO> paymentDTOS;
}
