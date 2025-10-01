package test.paymentapp.responses;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import test.DTOS.PaymentDTO;
import test.utils.CoreResponse;
import test.utils.ValidationError;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponseForPaymentParameters extends CoreResponse {

    private PaymentDTO paymentDTO;
    private List<PaymentDTO> paymentDTOS;
    private String status;

    public CommonResponseForPaymentParameters(List<ValidationError> errors) {
        super(errors);
    }
}
