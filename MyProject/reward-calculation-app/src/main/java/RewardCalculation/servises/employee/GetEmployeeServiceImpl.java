package RewardCalculation.servises.employee;

import RewardCalculation.JPA.domain.Employee;
import RewardCalculation.JPA.repositories.EmployeeRepository;
import RewardCalculation.dto.EmployeeDTO;
import RewardCalculation.requests.CommonRequestForEmployeeParameters;
import RewardCalculation.responses.CommonResponseForEmployeeParameters;
import RewardCalculation.util.ValidationError;
import RewardCalculation.validations.validators.employee.GetEmployeeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
class GetEmployeeServiceImpl implements GetEmployeeService {

    @Autowired private EmployeeRepository repository;

    @Autowired private GetEmployeeValidator validator;

    public CommonResponseForEmployeeParameters execute(CommonRequestForEmployeeParameters request){
        CommonResponseForEmployeeParameters response = new CommonResponseForEmployeeParameters();
        List<ValidationError> errors = validator.validate(request.getEmployeeDTO().getId());
        if (!errors.isEmpty()){
            response.setErrors(errors);
        }else {
            response.setEmployeeDTO(createEmployeeDTO(repository.findById(request.getEmployeeDTO().getId()).get()));
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
