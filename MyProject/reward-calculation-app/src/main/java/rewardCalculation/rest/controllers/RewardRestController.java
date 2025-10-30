package rewardCalculation.rest.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import rewardCalculation.EnumObject.RewardStatus;
import rewardCalculation.JPA.domain.Reward;
import rewardCalculation.JPA.repositories.RewardRepository;
import rewardCalculation.dto.RewardDTO;
import rewardCalculation.requests.CommonRequestForRewardParameters;
import rewardCalculation.responses.CommonResponseForRewardParameters;
import org.springframework.data.domain.Pageable;
import rewardCalculation.restClientRewardPayment.RewardPaymentResponse;
import rewardCalculation.rest.commonServiceInterfices.RewardCalculationService;
import rewardCalculation.lock.RewardExecutionLock;
import rewardCalculation.servises.reward.CreateRewardService;
import rewardCalculation.servises.reward.GetRewardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reward/calculation")
@RequiredArgsConstructor
@Slf4j
public class RewardRestController {

    private final CreateRewardService createRewardService;
    private final RewardRepository rewardRepository;
    private final RewardCalculationService rewardCalculationService;
    private final GetRewardService getRewardService;
    private final RewardExecutionLock rewardExecutionLock;

    @Value( "${app.reward_size}" )
    private int rewardSize;

    @PostMapping(path = "/createReward",
            consumes = "application/json",
            produces = "application/json")
    public CommonResponseForRewardParameters createReward(@RequestBody CommonRequestForRewardParameters request) {
        log.info("{} is start!",this.getClass().getSimpleName());
        CommonResponseForRewardParameters response = createRewardService.execute(request);
        log.info("{} is execute!",this.getClass().getSimpleName());
        return response;
    }

    @GetMapping(path = "/getReward/{id}",
            produces = "application/json")
    public CommonResponseForRewardParameters getReward(@PathVariable Long id) {
        log.info("{} is start!",this.getClass().getSimpleName());
        CommonRequestForRewardParameters request = CommonRequestForRewardParameters.builder().rewardDTO(RewardDTO.builder()
                .id(id).build()).build();
        CommonResponseForRewardParameters response = getRewardService.execute(request);
        log.info("{} is execute!",this.getClass().getSimpleName());
        return response;
    }

    @PostMapping(path = "/rewardCalculationExecute", produces = "application/json")
    public RewardPaymentResponse rewardCalculationExecute() {
        return rewardExecutionLock.runWithLock("rewardCalculation",() -> {
            log.info("▶️ RewardCalculationController: запуск расчёта наград");
            List<Reward> rewardList = rewardRepository.findByStatusWithEmployee(RewardStatus.UNPAID, Pageable.ofSize(rewardSize));
            log.debug("Get all Rewards which is not paid - {}" , rewardList);
            RewardPaymentResponse response = rewardCalculationService.execute(rewardList);
            log.info("✅ RewardCalculationController: завершено");
            return response;
        });
    }
}
