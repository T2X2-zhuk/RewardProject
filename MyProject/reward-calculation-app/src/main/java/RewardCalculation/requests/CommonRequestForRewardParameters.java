package RewardCalculation.requests;

import RewardCalculation.dto.EmployeeDTO;
import RewardCalculation.dto.RewardDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonRequestForRewardParameters {

    private RewardDTO rewardDTO;
}
