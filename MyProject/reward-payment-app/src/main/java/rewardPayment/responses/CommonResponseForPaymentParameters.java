package rewardPayment.responses;

import rewardPayment.dto.PaymentDTO;
import rewardPayment.util.forErrors.CoreResponse;
import rewardPayment.util.forErrors.ValidationError;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponseForPaymentParameters extends CoreResponse {

    private PaymentDTO paymentDTO;
    private List<PaymentDTO> paymentDTOS;
    private boolean successfulSaving;

    public CommonResponseForPaymentParameters(List<ValidationError> errors) {
        super(errors);
    }
}
