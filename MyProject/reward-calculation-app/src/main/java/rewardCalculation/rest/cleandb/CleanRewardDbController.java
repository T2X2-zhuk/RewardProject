package rewardCalculation.rest.cleandb;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.JPA.repositories.EmployeeRepository;
import rewardCalculation.JPA.repositories.RewardRepository;
import rewardCalculation.JPA.repositories.TariffRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rewardCalculation.cacheConfig.GetEmployeeUsingCache;
import rewardCalculation.cacheConfig.GetTariffUsingCache;
import rewardCalculation.servises.CleanDBService;

@RestController
@RequestMapping("/api/test/rewardDb")
@RequiredArgsConstructor
@Slf4j
public class CleanRewardDbController {

    private final CleanDBService cleanDBService;

    @PostMapping(path = "/cleanDb",
            consumes = "application/json",
            produces = "application/json")
    public CleanRewardDbResponse cleanDb(@RequestBody CleanRewardDbRequest request) {
        log.info("{} is start!",this.getClass().getSimpleName());
        CleanRewardDbResponse response = cleanDBService.execute(request);
        log.info("{} is execute!",this.getClass().getSimpleName());
        return response;
    }

}
