package rewardCalculation.rest.cleandb;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CleanRewardDbRequest {

    private boolean cleanEmployee;
    private boolean cleanReward;
    private boolean cleanTariff;

}
