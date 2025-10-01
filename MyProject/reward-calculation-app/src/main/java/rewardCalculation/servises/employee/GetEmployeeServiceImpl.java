package rewardCalculation.servises.employee;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.requests.CommonRequestForEmployeeParameters;
import rewardCalculation.responses.CommonResponseForEmployeeParameters;
import rewardCalculation.validations.validators.employee.GetEmployeeValidator;
import rewardCalculation.JPA.domain.Employee;
import rewardCalculation.JPA.repositories.EmployeeRepository;
import rewardCalculation.dto.EmployeeDTO;
import rewardCalculation.util.ValidationError;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
class GetEmployeeServiceImpl implements GetEmployeeService {

     private final EmployeeRepository repository;
     private final GetEmployeeValidator validator;

    public CommonResponseForEmployeeParameters execute(CommonRequestForEmployeeParameters request){
        log.info("{} is start!", this.getClass().getSimpleName());
        CommonResponseForEmployeeParameters response = new CommonResponseForEmployeeParameters();
        List<ValidationError> errors = validator.validate(request.getEmployeeDTO().getId());
        log.debug("Validation is execute!");
        if (!errors.isEmpty()){
            log.warn("Validation failed errors : {}" , errors);
            response.setErrors(errors);
        }else {
            response.setEmployeeDTO(createEmployeeDTO(repository.findById(request.getEmployeeDTO().getId()).get()));
            log.info("Employee : {} was successful received", response.getEmployeeDTO());
        }
        log.info("{} is execute!", this.getClass().getSimpleName());
        return response;
    }

    private EmployeeDTO createEmployeeDTO(Employee employee) {
        return EmployeeDTO.builder().id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .bonusCoefficient(employee.getBonusCoefficient()).build();
    }
}
