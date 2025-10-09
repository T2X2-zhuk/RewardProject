package rewardCalculation.servises.tariff;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import rewardCalculation.cacheConfig.LocalCacheConfig;
import rewardCalculation.util.ValidationError;
import rewardCalculation.validations.validators.tariff.CreateTariffRequestValidator;
import rewardCalculation.JPA.domain.Tariff;
import rewardCalculation.JPA.repositories.TariffRepository;
import rewardCalculation.dto.TariffDTO;
import rewardCalculation.requests.CommonRequestForTariffParameters;
import rewardCalculation.responses.CommonResponseForTariffParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
class CreateTariffServiceImpl implements CreateTariffService {


    private final TariffRepository tariffRepository;
    private final CreateTariffRequestValidator validator;
    private final CacheManager cacheManager;

    @Override
    public CommonResponseForTariffParameters execute(CommonRequestForTariffParameters request) {
        log.info("{} is start!", this.getClass().getSimpleName());
        CommonResponseForTariffParameters response = new CommonResponseForTariffParameters();
        List<ValidationError> errors = validator.validate(request);
        log.debug("Validation is execute!");
        if (errors.isEmpty()){
            Tariff tariff = buildTariff(request.getTariffDTO());
            saveTariffAndDeleteCacheTariffs(tariff);
            setTariffDTO(response,tariff);
            log.debug("All successful , tariff : {} is save!", tariff);
        }else {
            response.setErrors(errors);
            log.warn("Validation failed errors : {}" , errors);
        }
        log.info("{} is execute!", this.getClass().getSimpleName());
        return response;
    }

    private void saveTariffAndDeleteCacheTariffs(Tariff tariff){
        tariffRepository.save(tariff);
        Cache cache = cacheManager.getCache(LocalCacheConfig.TARIFF_CACHE);
        if (cache != null) {
            cache.clear();
            log.info("Cache 'tariffs' cleared!");
        } else {
            log.warn("Cache 'tariffs' not found!");
        }
    }
    private Tariff buildTariff(TariffDTO tariffDTO){
        return Tariff.builder().amount(tariffDTO.getAmount().setScale(2, RoundingMode.HALF_UP))
                .jobType(tariffDTO.getJobType().toUpperCase()).build();
    }
    private void setTariffDTO(CommonResponseForTariffParameters response,Tariff tariff){
        response.setTariffDTO(TariffDTO.builder()
                .id(tariff.getId())
                .amount(tariff.getAmount())
                .jobType(tariff.getJobType().toUpperCase()).build());
    }
}
