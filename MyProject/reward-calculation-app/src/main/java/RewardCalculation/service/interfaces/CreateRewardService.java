package RewardCalculation.service.interfaces;

import RewardCalculation.requests.CreateRewardRequest;
import RewardCalculation.responses.CreateRewardResponse;

public interface CreateRewardService {

   CreateRewardResponse execute(CreateRewardRequest request);

}
