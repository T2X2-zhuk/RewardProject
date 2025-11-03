package rewardCalculation.rest.commonServiceInterfices;

import rewardCalculation.jpa.domain.Reward;
import rewardCalculation.restClientRewardPayment.RewardPaymentResponse;

import java.util.List;

public interface RewardCalculationService {

    RewardPaymentResponse execute(List<Reward> rewardList);
}
