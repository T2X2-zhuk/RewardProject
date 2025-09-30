package RewardPayment.responses;

import RewardPayment.dto.PaymentDTO;
import RewardPayment.util.CoreResponse;
import RewardPayment.util.ValidationError;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponseForPaymentParameters extends CoreResponse {

    private PaymentDTO paymentDTO;
    private List<PaymentDTO> paymentDTOS;
    private String status;

    public CommonResponseForPaymentParameters(List<ValidationError> errors) {
        super(errors);
    }
}
