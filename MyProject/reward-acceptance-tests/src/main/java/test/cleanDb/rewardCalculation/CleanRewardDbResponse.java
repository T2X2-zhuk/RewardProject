package test.cleanDb.rewardCalculation;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CleanRewardDbResponse {

    private boolean employeeDeleted;
    private boolean rewardDeleted;
    private boolean tariffDeleted;

}
