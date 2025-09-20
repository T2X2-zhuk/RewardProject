package RewardPayment.responses;

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
public class RewardPaymentResponse extends CoreResponse {

    private String status;

    public RewardPaymentResponse(List<ValidationError> errors) {
        super(errors);
    }
}
