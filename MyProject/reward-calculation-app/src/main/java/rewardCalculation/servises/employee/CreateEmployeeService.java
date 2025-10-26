package rewardCalculation.servises.employee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.cache.get.GetEmployeeUsingCache;
import rewardCalculation.requests.CommonRequestForEmployeeParameters;
import rewardCalculation.responses.CommonResponseForEmployeeParameters;
import rewardCalculation.util.forError.ValidationError;
import rewardCalculation.validations.validators.employee.CreateEmployeeRequestValidator;
import rewardCalculation.JPA.domain.Employee;
import rewardCalculation.JPA.repositories.EmployeeRepository;
import rewardCalculation.dto.EmployeeDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final CreateEmployeeRequestValidator validator;
    private final GetEmployeeUsingCache getEmployeeUsingCache;

    @Transactional
    public CommonResponseForEmployeeParameters execute(CommonRequestForEmployeeParameters request) {
        log.info("{} is start!", this.getClass().getSimpleName());
        CommonResponseForEmployeeParameters response = new CommonResponseForEmployeeParameters();
        List<ValidationError> errors = validator.validate(request);
        log.debug("Validation is execute!");
        if (errors.isEmpty()) {
            Employee employee = buildEmployee(request.getEmployeeDTO());
            employeeRepository.save(employee);
            getEmployeeUsingCache.clearEMPLOYEESCache();
            log.debug("Employee : {} successful saved!",employee);
            setEmployeeDTO(response,employee);
        } else {
            response.setErrors(errors);
            log.warn("Validation failed errors : {}" , errors);
        }
        log.info("{} is execute!", this.getClass().getSimpleName());
        return response;
    }
    private Employee buildEmployee(EmployeeDTO employeeDTO){
        return Employee.builder()
                .firstName(employeeDTO.getFirstName())
                .lastName(employeeDTO.getLastName())
                .bonusCoefficient(employeeDTO.getBonusCoefficient()).build();
    }
    private void setEmployeeDTO(CommonResponseForEmployeeParameters response, Employee employee){
        response.setEmployeeDTO(EmployeeDTO.builder().id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .bonusCoefficient(employee.getBonusCoefficient()).build());
    }
}
