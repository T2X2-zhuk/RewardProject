package RewardCalculation.servises.reward;

import RewardCalculation.requests.CommonRequestForRewardParameters;
import RewardCalculation.responses.CommonResponseForRewardParameters;

public interface CreateRewardService {

   CommonResponseForRewardParameters execute(CommonRequestForRewardParameters request);

}
