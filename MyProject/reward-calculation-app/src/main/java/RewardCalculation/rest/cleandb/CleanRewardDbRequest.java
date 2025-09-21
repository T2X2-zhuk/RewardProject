package RewardCalculation.rest.cleandb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CleanRewardDbRequest {

    private boolean cleanEmployee;
    private boolean cleanReward;
    private boolean cleanTariff;

}
