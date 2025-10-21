package rewardCalculation.responses;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CleanRewardDbResponse {

    private boolean jobTypeDelete;
    private boolean employeeDeleted;
    private boolean rewardDeleted;
    private boolean tariffDeleted;
    private boolean outboxPaymentEventDeleted;
}
