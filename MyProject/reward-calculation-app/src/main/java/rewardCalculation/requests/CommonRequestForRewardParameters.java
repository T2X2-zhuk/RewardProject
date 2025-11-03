package rewardCalculation.requests;

import rewardCalculation.dto.RewardDTO;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonRequestForRewardParameters {

    private RewardDTO rewardDTO;
}
