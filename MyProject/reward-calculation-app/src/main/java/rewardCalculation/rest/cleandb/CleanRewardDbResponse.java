package rewardCalculation.rest.cleandb;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CleanRewardDbResponse {

    private boolean employeeDeleted;
    private boolean rewardDeleted;
    private boolean tariffDeleted;
    private boolean outboxPaymentEventDeleted;
}
