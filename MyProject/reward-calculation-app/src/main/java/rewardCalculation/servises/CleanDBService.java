package rewardCalculation.servises;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import rewardCalculation.JPA.repositories.EmployeeRepository;
import rewardCalculation.JPA.repositories.RewardRepository;
import rewardCalculation.JPA.repositories.TariffRepository;
import rewardCalculation.cacheConfig.GetEmployeeUsingCache;
import rewardCalculation.cacheConfig.GetTariffUsingCache;
import rewardCalculation.rest.cleandb.CleanRewardDbRequest;
import rewardCalculation.rest.cleandb.CleanRewardDbResponse;

@Component
@RequiredArgsConstructor
@Slf4j
public class CleanDBService {

    private final EmployeeRepository employeeRepository;
    private final RewardRepository rewardRepository;
    private final TariffRepository tariffRepository;

    private final GetTariffUsingCache getTariffUsingCache;
    private final GetEmployeeUsingCache getEmployeeUsingCache;

    @Transactional
    public CleanRewardDbResponse execute(CleanRewardDbRequest request) {
        log.info("{} is start!",this.getClass().getSimpleName());
        CleanRewardDbResponse response = new CleanRewardDbResponse();
        if (request.isCleanTariff()) {
            tariffRepository.deleteAll();
            response.setTariffDeleted(true);
            getTariffUsingCache.clearTARIFF_CACHECache();
            log.debug("Clean db tariff and cache");
        }
        if (request.isCleanReward()) {
            rewardRepository.deleteAll();
            response.setRewardDeleted(true);
            log.debug("Clean db reward");
        }
        if (request.isCleanEmployee()) {
            employeeRepository.deleteAll();
            response.setEmployeeDeleted(true);
            getEmployeeUsingCache.clearEMPLOYEESCache();
            log.debug("Clean db employee and cache");
        }
        log.info("{} is execute!",this.getClass().getSimpleName());
        return response;
    }
}
