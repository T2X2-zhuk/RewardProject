package rewardCalculation.rest.cleandb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CleanRewardDbResponse {

    private boolean employeeDeleted;
    private boolean rewardDeleted;
    private boolean tariffDeleted;

}
