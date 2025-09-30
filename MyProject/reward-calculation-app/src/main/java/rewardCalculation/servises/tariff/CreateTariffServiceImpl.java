package rewardCalculation.servises.tariff;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.util.ValidationError;
import rewardCalculation.validations.validators.tariff.CreateTariffRequestValidator;
import rewardCalculation.JPA.domain.Tariff;
import rewardCalculation.JPA.repositories.TariffRepository;
import rewardCalculation.dto.TariffDTO;
import rewardCalculation.requests.CommonRequestForTariffParameters;
import rewardCalculation.responses.CommonResponseForTariffParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@ToString
@Slf4j
class CreateTariffServiceImpl implements CreateTariffService {


    private final TariffRepository tariffRepository;
    private final CreateTariffRequestValidator validator;

    @Override
    public CommonResponseForTariffParameters execute(CommonRequestForTariffParameters request) {
        log.info("{} is start!", this);
        CommonResponseForTariffParameters response = new CommonResponseForTariffParameters();
        List<ValidationError> errors = validator.validate(request);
        log.debug("Validation is execute!");
        if (errors.isEmpty()){
            Tariff tariff = Tariff.builder().amount(request.getTariffDTO().getAmount())
                    .jobType(request.getTariffDTO().getJobType()).build();
            tariffRepository.save(tariff);
            response.setTariffDTO(TariffDTO.builder()
                    .id(tariff.getId())
                    .amount(tariff.getAmount())
                    .jobType(tariff.getJobType()).build());
            log.debug("All successful , tariff : {} is save!", tariff);
        }else {
            response.setErrors(errors);
            log.warn("Validation failed errors : {}" , errors);
        }
        log.info("{} is execute!", this);
        return response;
    }
}
