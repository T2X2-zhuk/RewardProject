package rewardCalculation.rest;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
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
@ToString
@Slf4j
public class RewardRestController {

    private final CreateRewardService createRewardService;
    private final EmployeeRepository repository;
    private final RewardCalculationService rewardCalculationService;
    private final GetRewardService getRewardService;

    @PostMapping(path = "/createReward",
            consumes = "application/json",
            produces = "application/json")
    public CommonResponseForRewardParameters createReward(@RequestBody CommonRequestForRewardParameters request) {
        log.info("{} is start!",this);
        CommonResponseForRewardParameters response = createRewardService.execute(request);
        log.info("{} is execute!",this);
        return response;
    }

    @GetMapping(path = "/getReward/{id}",
            produces = "application/json")
    public CommonResponseForRewardParameters getReward(@PathVariable Long id) {
        log.info("{} is start!",this);
        CommonRequestForRewardParameters request = CommonRequestForRewardParameters.builder().rewardDTO(RewardDTO.builder()
                .id(id).build()).build();
        CommonResponseForRewardParameters response = getRewardService.execute(request);
        log.info("{} is execute!",this);
        return response;
    }

    @PostMapping(path = "/rewardCalculationExecute",
            produces = "application/json")
    public RewardPaymentResponse rewardCalculationExecute() {
        log.info("{} is start!",this);
        RewardPaymentResponse response = rewardCalculationService.execute(repository.findAll());
        log.info("{} is execute!",this);
        return response;
    }
}
