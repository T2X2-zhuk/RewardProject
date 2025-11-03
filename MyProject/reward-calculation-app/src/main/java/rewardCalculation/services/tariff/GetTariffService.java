package rewardCalculation.services.tariff;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.jpa.domain.Tariff;
import rewardCalculation.jpa.repositories.TariffRepository;
import rewardCalculation.dto.TariffDTO;
import rewardCalculation.requests.CommonRequestForTariffParameters;
import rewardCalculation.responses.CommonResponseForTariffParameters;
import rewardCalculation.util.forErrors.ValidationError;
import rewardCalculation.validations.validators.tariff.GetTariffValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetTariffService {

    private final GetTariffValidator validator;
    private final TariffRepository tariffRepository;

    @Transactional
    public CommonResponseForTariffParameters execute(CommonRequestForTariffParameters request) {
        log.info("{} is start!", this.getClass().getSimpleName());

        CommonResponseForTariffParameters response = new CommonResponseForTariffParameters();

        List<ValidationError> errors = validator.validate(request.getTariffDTO().getId());

        if (!errors.isEmpty()){
            response.setErrors(errors);
            log.warn("Validation failed errors : {}" , errors);
        }else {
            Optional<Tariff> tariff = tariffRepository.findById(request.getTariffDTO().getId());

            response.setTariffDTO(createTariffDTO(tariff.get()));
            log.debug("Received tariff : {}", tariff);
        }

        log.info("{} is execute!", this.getClass().getSimpleName());
        return response;
    }

    private TariffDTO createTariffDTO(Tariff tariff) {
        return TariffDTO.builder().id(tariff.getId())
                .jobType(tariff.getJobType().getJobType()).amount(tariff.getAmount()).build();
    }
}
