package rewardCalculation.responses;


import rewardCalculation.dto.RewardDTO;
import rewardCalculation.util.CoreResponse;
import rewardCalculation.util.ValidationError;
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
