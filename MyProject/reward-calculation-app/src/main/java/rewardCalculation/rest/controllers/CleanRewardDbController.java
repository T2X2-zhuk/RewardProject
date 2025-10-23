package rewardCalculation.rest.controllers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rewardCalculation.requests.CleanRewardDbRequest;
import rewardCalculation.responses.CleanRewardDbResponse;
import rewardCalculation.servises.CleanDBService;
import rewardCalculation.lock.RewardExecutionLock;

@RestController
@RequestMapping("/api/test/rewardDb")
@RequiredArgsConstructor
@Slf4j
public class CleanRewardDbController {

    private final CleanDBService cleanDBService;
    private final RewardExecutionLock rewardExecutionLock;

    @PostMapping(path = "/cleanDb",
            consumes = "application/json",
            produces = "application/json")
    public CleanRewardDbResponse cleanDb(@RequestBody CleanRewardDbRequest request) {
        return rewardExecutionLock.runWithLock("cleanDb",()->{
            log.info("{} is start!",this.getClass().getSimpleName());
            CleanRewardDbResponse response = cleanDBService.execute(request);
            log.info("{} is execute!",this.getClass().getSimpleName());
            return response;
        });
    }

}
