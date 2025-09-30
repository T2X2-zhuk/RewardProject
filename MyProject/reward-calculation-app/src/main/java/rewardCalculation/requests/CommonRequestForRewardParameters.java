package rewardCalculation.requests;

import rewardCalculation.dto.RewardDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CommonRequestForRewardParameters {

    private RewardDTO rewardDTO;
}
