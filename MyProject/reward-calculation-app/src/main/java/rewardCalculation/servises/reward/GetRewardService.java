package rewardCalculation.servises.reward;

import rewardCalculation.responses.CommonResponseForRewardParameters;
import rewardCalculation.requests.CommonRequestForRewardParameters;

public interface GetRewardService {

    CommonResponseForRewardParameters execute(CommonRequestForRewardParameters request);
}
