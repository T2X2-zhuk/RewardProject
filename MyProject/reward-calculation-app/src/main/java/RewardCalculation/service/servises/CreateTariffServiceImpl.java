package RewardCalculation.service.servises;

import RewardCalculation.JPA.domain.Employee;
import RewardCalculation.JPA.domain.Tariff;
import RewardCalculation.JPA.repositories.EmployeeRepository;
import RewardCalculation.JPA.repositories.TariffRepository;
import RewardCalculation.dto.EmployeeDTO;
import RewardCalculation.requests.CreateEmployeeRequest;
import RewardCalculation.requests.CreateTariffRequest;
import RewardCalculation.responses.CreateEmployeeResponse;
import RewardCalculation.responses.CreateTariffResponse;
import RewardCalculation.service.interfaces.CreateEmployeeService;
import RewardCalculation.service.interfaces.CreateTariffService;
import RewardCalculation.validations.validators.CreateEmployeeRequestValidator;
import RewardCalculation.validations.validators.CreateTariffRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class CreateTariffServiceImpl implements CreateTariffService {

    @Autowired
    private TariffRepository tariffRepository;

    @Autowired private CreateTariffRequestValidator validator;

    @Override
    public CreateTariffResponse execute(CreateTariffRequest request) {
        CreateTariffResponse response = new CreateTariffResponse();
        if (validator.validate(request).isEmpty()){
            Tariff tariff = Tariff.builder().amount(request.getTariffDTO().getAmount())
                    .jobType(request.getTariffDTO().getJobType()).build();
            tariffRepository.save(tariff);
            response.setTariffDTO(request.getTariffDTO());
        }else {
            response.setErrors(validator.validate(request));
        }
        return response;
    }
}
