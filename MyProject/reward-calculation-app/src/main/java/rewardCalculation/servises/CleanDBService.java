package rewardCalculation.servises;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import rewardCalculation.JPA.repositories.*;
import rewardCalculation.cacheConfig.GetEmployeeUsingCache;
import rewardCalculation.cacheConfig.GetTariffUsingCache;
import rewardCalculation.requests.CleanRewardDbRequest;
import rewardCalculation.responses.CleanRewardDbResponse;
import rewardCalculation.transactionalOutBox.JPA.OutboxPaymentEventRepository;


@Component
@RequiredArgsConstructor
@Slf4j
public class CleanDBService {

    private final EmployeeRepository employeeRepository;
    private final RewardRepository rewardRepository;
    private final TariffRepository tariffRepository;
    private final OutboxPaymentEventRepository repository;
    private final GetTariffUsingCache getTariffUsingCache;
    private final GetEmployeeUsingCache getEmployeeUsingCache;
    private final JobTypeEntityRepository jobTypeEntityRepository;

    @Transactional
    public CleanRewardDbResponse execute(CleanRewardDbRequest request) {
        log.info("{} is start!", this.getClass().getSimpleName());
        CleanRewardDbResponse response = new CleanRewardDbResponse();

        if (request.isCleanReward()) {
            rewardRepository.deleteAll();
            response.setRewardDeleted(true);
            log.debug("Clean db reward");
        }

        if (request.isCleanTariff()) {
            tariffRepository.deleteAll();
            response.setTariffDeleted(true);
            getTariffUsingCache.clearTARIFF_CACHECache();
            log.debug("Clean db tariff and cache");
        }

        if (request.isCleanEmployee()) {
            employeeRepository.deleteAll();
            response.setEmployeeDeleted(true);
            getEmployeeUsingCache.clearEMPLOYEESCache();
            log.debug("Clean db employee and cache");
        }

        if (request.isCleanJobType()) {
            jobTypeEntityRepository.deleteAll();
            response.setJobTypeDelete(true);
            log.debug("Clean db job type and cache");
        }

        if (request.isCleanOutboxPaymentEvent()) {
            repository.deleteAll();
            response.setOutboxPaymentEventDeleted(true);
            log.debug("Clean db OutboxPaymentEvent");
        }
        log.info("{} is execute!", this.getClass().getSimpleName());
        return response;
    }
}
