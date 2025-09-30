package rewardCalculation.servises.tariff;

import lombok.RequiredArgsConstructor;
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

@Service
@Transactional
@RequiredArgsConstructor
class GetTariffServiceImpl implements GetTariffService {

    private final GetTariffValidator validator;
    private final TariffRepository repository;
    @Override
    public CommonResponseForTariffParameters execute(CommonRequestForTariffParameters request) {
        CommonResponseForTariffParameters response = new CommonResponseForTariffParameters();
        List<ValidationError> errors = validator.validate(request.getTariffDTO().getId());
        if (!errors.isEmpty()){
            response.setErrors(errors);
        }else {
            response.setTariffDTO(createTariffDTO(repository.findById(request.getTariffDTO().getId()).get()));
        }
        return response;
    }

    private TariffDTO createTariffDTO(Tariff tariff) {
        return TariffDTO.builder().id(tariff.getId())
                .jobType(tariff.getJobType()).amount(tariff.getAmount()).build();
    }
}
