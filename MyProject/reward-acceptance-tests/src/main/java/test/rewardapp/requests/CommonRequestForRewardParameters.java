package test.rewardapp.requests;

import lombok.*;
import test.DTOS.RewardDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CommonRequestForRewardParameters {

    private RewardDTO rewardDTO;
}
