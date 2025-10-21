package test.cleanDb.rewardCalculation;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CleanRewardDbRequest {

    private boolean cleanJobType;
    private boolean cleanEmployee;
    private boolean cleanReward;
    private boolean cleanTariff;
    private boolean cleanOutboxPaymentEvent;
}
