package rewardCalculation.requests;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CleanRewardDbRequest {

    private boolean cleanJobType;
    private boolean cleanEmployee;
    private boolean cleanReward;
    private boolean cleanTariff;
    private boolean cleanOutboxPaymentEvent;
}
