package rewardCalculation.servises.tariff;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.cacheConfig.GetTariffUsingCache;
import rewardCalculation.util.forError.ValidationError;
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
@RequiredArgsConstructor
@Slf4j
class CreateTariffServiceImpl implements CreateTariffService {


    private final TariffRepository tariffRepository;
    private final CreateTariffRequestValidator validator;
    private final GetTariffUsingCache getTariffUsingCache;
    @Transactional
    @Override
    public CommonResponseForTariffParameters execute(CommonRequestForTariffParameters request) {
        log.info("{} is start!", this.getClass().getSimpleName());
        CommonResponseForTariffParameters response = new CommonResponseForTariffParameters();
        List<ValidationError> errors = validator.validate(request);
        log.debug("Validation is execute!");
        if (errors.isEmpty()){
            Tariff tariff = buildTariff(request.getTariffDTO());
            tariffRepository.save(tariff);
            getTariffUsingCache.clearTARIFF_CACHECache();
            setTariffDTO(response,tariff);
            log.debug("All successful , tariff : {} is save!", tariff);
        }else {
            response.setErrors(errors);
            log.warn("Validation failed errors : {}" , errors);
        }
        log.info("{} is execute!", this.getClass().getSimpleName());
        return response;
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
