package RewardCalculation.servises.tariff;

import RewardCalculation.JPA.domain.Tariff;
import RewardCalculation.JPA.repositories.TariffRepository;
import RewardCalculation.dto.TariffDTO;
import RewardCalculation.requests.CommonRequestForTariffParameters;
import RewardCalculation.responses.CommonResponseForTariffParameters;
import RewardCalculation.util.ValidationError;
import RewardCalculation.validations.validators.tariff.GetTariffValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
class GetTariffServiceImpl implements GetTariffService {

    @Autowired private GetTariffValidator validator;
    @Autowired private TariffRepository repository;
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
