package RewardCalculation.responses;


import RewardCalculation.dto.RewardDTO;
import RewardCalculation.util.CoreResponse;
import RewardCalculation.util.ValidationError;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponseForRewardParameters extends CoreResponse {

    private RewardDTO rewardDTO;

    public CommonResponseForRewardParameters(List<ValidationError> errors) {
        super(errors);
    }
}
