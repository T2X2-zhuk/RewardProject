package rewardPayment.requests;

import rewardPayment.dto.PaymentDTO;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonRequestForPaymentParameters {

    private PaymentDTO paymentDTO;
    private List<PaymentDTO> paymentDTOS;
}
