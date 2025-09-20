package RewardCalculation.service.servises;

import RewardCalculation.JPA.domain.Employee;
import RewardCalculation.JPA.repositories.EmployeeRepository;
import RewardCalculation.dto.EmployeeDTO;
import RewardCalculation.responses.GetEmployeeResponse;
import RewardCalculation.service.interfaces.GetEmployeeService;
import RewardCalculation.util.ValidationError;
import RewardCalculation.validations.validators.GetEmployeeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
class GetEmployeeServiceImpl implements GetEmployeeService {

    @Autowired private EmployeeRepository repository;

    @Autowired private GetEmployeeValidator validator;

    public GetEmployeeResponse execute(Long id){
        GetEmployeeResponse response = new GetEmployeeResponse();
        List<ValidationError> errors = validator.validate(id);
        if (!errors.isEmpty()){
            response.setErrors(errors);
        }else {
            response.setEmployeeDTO(createEmployeeDTO(repository.findById(id).get()));
        }
        return response;
    }

    private EmployeeDTO createEmployeeDTO(Employee employee) {
        return EmployeeDTO.builder().id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .bonusCoefficient(employee.getBonusCoefficient()).build();
    }
}
