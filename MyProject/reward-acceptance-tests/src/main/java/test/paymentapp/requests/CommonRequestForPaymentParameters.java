package test.paymentapp.requests;
import lombok.*;
import test.DTOS.PaymentDTO;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonRequestForPaymentParameters {

    private PaymentDTO paymentDTO;
    private List<PaymentDTO> paymentDTOS;
}
