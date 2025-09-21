package RewardCalculation.rest.reward;

import RewardCalculation.requests.CreateRewardRequest;
import RewardCalculation.responses.CreateRewardResponse;
import RewardCalculation.service.interfaces.CreateRewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/reward")
public class CreateRewardRestController {

    @Autowired private CreateRewardService service;

    @PostMapping(path = "/createReward",
            consumes = "application/json",
            produces = "application/json")
    public CreateRewardResponse createReward(@RequestBody CreateRewardRequest request) {
        return service.execute(request);
    }
}
