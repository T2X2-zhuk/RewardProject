package rewardCalculation.rest;

import lombok.RequiredArgsConstructor;
import rewardCalculation.JPA.repositories.EmployeeRepository;
import rewardCalculation.dto.RewardDTO;
import rewardCalculation.requests.CommonRequestForRewardParameters;
import rewardCalculation.responses.CommonResponseForRewardParameters;

import rewardCalculation.restClientRewardPayment.RewardPaymentResponse;
import rewardCalculation.servises.reward.CreateRewardService;
import rewardCalculation.servises.reward.GetRewardService;
import rewardCalculation.servises.reward.RewardCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reward/calculation")
@RequiredArgsConstructor
public class RewardRestController {

    private final CreateRewardService createRewardService;
    private final EmployeeRepository repository;
    private final RewardCalculationService rewardCalculationService;
    private final GetRewardService getRewardService;

    @PostMapping(path = "/createReward",
            consumes = "application/json",
            produces = "application/json")
    public CommonResponseForRewardParameters createReward(@RequestBody CommonRequestForRewardParameters request) {
        return createRewardService.execute(request);
    }

    @GetMapping(path = "/getReward/{id}",
            produces = "application/json")
    public CommonResponseForRewardParameters getReward(@PathVariable Long id) {
        CommonRequestForRewardParameters request = CommonRequestForRewardParameters.builder().rewardDTO(RewardDTO.builder()
                .id(id).build()).build();
        return getRewardService.execute(request);
    }

    @PostMapping(path = "/rewardCalculationExecute",
            produces = "application/json")
    public RewardPaymentResponse rewardCalculationExecute() {
        return rewardCalculationService.execute(repository.findAll());
    }
}
