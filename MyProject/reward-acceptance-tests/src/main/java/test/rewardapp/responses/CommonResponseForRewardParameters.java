package test.rewardapp.responses;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import test.DTOS.RewardDTO;
import test.utils.CoreResponse;
import test.utils.ValidationError;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponseForRewardParameters extends CoreResponse {

    private RewardDTO rewardDTO;

    public CommonResponseForRewardParameters(List<ValidationError> errors) {
        super(errors);
    }
}
