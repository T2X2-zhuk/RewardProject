package rewardCalculation.servises.reward;

import rewardCalculation.responses.CommonResponseForRewardParameters;
import rewardCalculation.requests.CommonRequestForRewardParameters;

public interface CreateRewardService {

   CommonResponseForRewardParameters execute(CommonRequestForRewardParameters request);

}
