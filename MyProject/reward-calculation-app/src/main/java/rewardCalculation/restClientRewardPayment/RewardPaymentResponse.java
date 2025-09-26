package rewardCalculation.restClientRewardPayment;

import rewardCalculation.dto.PaymentDTO;
import rewardCalculation.util.CoreResponse;
import rewardCalculation.util.ValidationError;
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
    private List<PaymentDTO> paymentDTOS;

    public RewardPaymentResponse(List<ValidationError> errors) {
        super(errors);
    }
}
