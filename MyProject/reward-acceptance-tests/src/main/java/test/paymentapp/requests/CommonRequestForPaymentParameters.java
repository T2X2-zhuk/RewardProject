package test.paymentapp.requests;
import lombok.*;
import test.DTOS.PaymentDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonRequestForPaymentParameters {

    private PaymentDTO paymentDTO;
    private List<PaymentDTO> paymentDTOS;
}
