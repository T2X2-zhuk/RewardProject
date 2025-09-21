package RewardCalculation.service.servises;

import RewardCalculation.JPA.domain.Employee;
import RewardCalculation.JPA.domain.Tariff;
import RewardCalculation.JPA.repositories.TariffRepository;
import RewardCalculation.dto.EmployeeDTO;
import RewardCalculation.dto.TariffDTO;
import RewardCalculation.responses.GetEmployeeResponse;
import RewardCalculation.responses.GetTariffResponse;
import RewardCalculation.service.interfaces.GetTariffService;
import RewardCalculation.util.ValidationError;
import RewardCalculation.validations.validators.GetTariffValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GetTariffServiceImpl implements GetTariffService {

    @Autowired private GetTariffValidator validator;
    @Autowired private TariffRepository repository;
    @Override
    public GetTariffResponse execute(Long id) {
        GetTariffResponse response = new GetTariffResponse();
        List<ValidationError> errors = validator.validate(id);
        if (!errors.isEmpty()){
            response.setErrors(errors);
        }else {
            response.setTariffDTO(createTariffDTO(repository.findById(id).get()));
        }
        return response;
    }

    private TariffDTO createTariffDTO(Tariff tariff) {
        return TariffDTO.builder().id(tariff.getId())
                .jobType(tariff.getJobType()).amount(tariff.getAmount()).build();
    }
}
