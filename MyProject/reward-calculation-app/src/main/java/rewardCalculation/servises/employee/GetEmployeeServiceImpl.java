package rewardCalculation.servises.employee;

import lombok.RequiredArgsConstructor;
import rewardCalculation.requests.CommonRequestForEmployeeParameters;
import rewardCalculation.responses.CommonResponseForEmployeeParameters;
import rewardCalculation.validations.validators.employee.GetEmployeeValidator;
import rewardCalculation.JPA.domain.Employee;
import rewardCalculation.JPA.repositories.EmployeeRepository;
import rewardCalculation.dto.EmployeeDTO;
import rewardCalculation.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
class GetEmployeeServiceImpl implements GetEmployeeService {

     private final EmployeeRepository repository;
     private final GetEmployeeValidator validator;

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
