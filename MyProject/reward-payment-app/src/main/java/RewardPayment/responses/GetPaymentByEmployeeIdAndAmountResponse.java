package RewardPayment.responses;

import RewardPayment.dto.PaymentDTO;
import RewardPayment.util.CoreResponse;
import RewardPayment.util.ValidationError;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetPaymentByEmployeeIdAndAmountResponse extends CoreResponse {

    private PaymentDTO paymentDTO;

    public GetPaymentByEmployeeIdAndAmountResponse(List<ValidationError> errors) {
        super(errors);
    }
}
