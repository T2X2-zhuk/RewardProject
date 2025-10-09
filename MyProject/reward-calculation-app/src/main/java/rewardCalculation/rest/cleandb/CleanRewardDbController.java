package rewardCalculation.rest.cleandb;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import rewardCalculation.JPA.repositories.EmployeeRepository;
import rewardCalculation.JPA.repositories.RewardRepository;
import rewardCalculation.JPA.repositories.TariffRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rewardCalculation.cacheConfig.GetEmployeeUsingCache;
import rewardCalculation.cacheConfig.GetTariffUsingCache;
import rewardCalculation.cacheConfig.RedisCacheConfig;

@RestController
@RequestMapping("/api/test/rewardDb")
@RequiredArgsConstructor
@Slf4j
public class CleanRewardDbController {

    private final EmployeeRepository employeeRepository;
    private final RewardRepository rewardRepository;
    private final TariffRepository tariffRepository;
    private final GetTariffUsingCache getTariffUsingCache;
    private final GetEmployeeUsingCache getEmployeeUsingCache;

    @PostMapping(path = "/cleanDb",
            consumes = "application/json",
            produces = "application/json")
    public CleanRewardDbResponse cleanDb(@RequestBody CleanRewardDbRequest request) {
        log.info("{} is start!",this.getClass().getSimpleName());
        CleanRewardDbResponse response = new CleanRewardDbResponse();
        if (request.isCleanTariff()) {
            tariffRepository.deleteAll();
            response.setTariffDeleted(true);
           getTariffUsingCache.clearTARIFF_CACHECache();
        }
        if (request.isCleanReward()) {
            rewardRepository.deleteAll();
            response.setRewardDeleted(true);
        }
        if (request.isCleanEmployee()) {
            employeeRepository.deleteAll();
            response.setEmployeeDeleted(true);
            getEmployeeUsingCache.clearEMPLOYEESCache();
        }
        log.info("{} is execute!",this.getClass().getSimpleName());
        return response;
    }

}
