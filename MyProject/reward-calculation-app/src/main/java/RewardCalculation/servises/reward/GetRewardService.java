package RewardCalculation.servises.reward;

import RewardCalculation.requests.CommonRequestForRewardParameters;
import RewardCalculation.responses.CommonResponseForRewardParameters;
public interface GetRewardService {

    CommonResponseForRewardParameters execute(CommonRequestForRewardParameters request);
}
