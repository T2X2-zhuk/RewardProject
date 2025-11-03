package test.rewardapp.requests;

import lombok.*;
import test.DTOS.RewardDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonRequestForRewardParameters {

    private RewardDTO rewardDTO;
}
