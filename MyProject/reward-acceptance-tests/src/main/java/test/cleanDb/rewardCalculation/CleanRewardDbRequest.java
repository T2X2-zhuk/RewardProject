package test.cleanDb.rewardCalculation;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CleanRewardDbRequest {

    private boolean cleanEmployee;
    private boolean cleanReward;
    private boolean cleanTariff;

}
