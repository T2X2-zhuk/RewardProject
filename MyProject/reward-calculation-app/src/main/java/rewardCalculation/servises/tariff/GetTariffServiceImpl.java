package rewardCalculation.servises.tariff;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.JPA.domain.Tariff;
import rewardCalculation.JPA.repositories.TariffRepository;
import rewardCalculation.dto.TariffDTO;
import rewardCalculation.requests.CommonRequestForTariffParameters;
import rewardCalculation.responses.CommonResponseForTariffParameters;
import rewardCalculation.util.ValidationError;
import rewardCalculation.validations.validators.tariff.GetTariffValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@ToString
@Slf4j
class GetTariffServiceImpl implements GetTariffService {

    private final GetTariffValidator validator;
    private final TariffRepository repository;
    @Override
    public CommonResponseForTariffParameters execute(CommonRequestForTariffParameters request) {
        log.info("{} is start!", this);
        CommonResponseForTariffParameters response = new CommonResponseForTariffParameters();
        List<ValidationError> errors = validator.validate(request.getTariffDTO().getId());
        if (!errors.isEmpty()){
            response.setErrors(errors);
            log.warn("Validation failed errors : {}" , errors);
        }else {
            Optional<Tariff> tariff = repository.findById(request.getTariffDTO().getId());
            response.setTariffDTO(createTariffDTO(tariff.get()));
            log.debug("Received tariff : {}", tariff);
        }
        log.info("{} is execute!", this);
        return response;
    }

    private TariffDTO createTariffDTO(Tariff tariff) {
        return TariffDTO.builder().id(tariff.getId())
                .jobType(tariff.getJobType()).amount(tariff.getAmount()).build();
    }
}
