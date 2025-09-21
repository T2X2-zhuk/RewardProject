package RewardCalculation.requests;

import RewardCalculation.JPA.domain.Reward;
import RewardCalculation.dto.RewardDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRewardRequest {

    private RewardDTO rewardDTO;

}
