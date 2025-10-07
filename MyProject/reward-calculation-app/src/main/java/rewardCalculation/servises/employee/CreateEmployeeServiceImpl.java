package rewardCalculation.servises.employee;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.requests.CommonRequestForEmployeeParameters;
import rewardCalculation.responses.CommonResponseForEmployeeParameters;
import rewardCalculation.util.ValidationError;
import rewardCalculation.validations.validators.employee.CreateEmployeeRequestValidator;
import rewardCalculation.JPA.domain.Employee;
import rewardCalculation.JPA.repositories.EmployeeRepository;
import rewardCalculation.dto.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
class CreateEmployeeServiceImpl implements CreateEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final CreateEmployeeRequestValidator validator;

    public CommonResponseForEmployeeParameters execute(CommonRequestForEmployeeParameters request) {
        log.info("{} is start!", this.getClass().getSimpleName());
        CommonResponseForEmployeeParameters response = new CommonResponseForEmployeeParameters();
        List<ValidationError> errors = validator.validate(request);
        log.debug("Validation is execute!");
        if (errors.isEmpty()) {
            Employee employee = Employee.builder()
                    .firstName(request.getEmployeeDTO().getFirstName())
                    .lastName(request.getEmployeeDTO().getLastName())
                    .bonusCoefficient(request.getEmployeeDTO().getBonusCoefficient().setScale(2, RoundingMode.HALF_UP)).build();
            employeeRepository.save(employee);
            log.debug("Employee: {} successful saved!", employee);
            response.setEmployeeDTO(EmployeeDTO.builder().id(employee.getId())
                    .firstName(employee.getFirstName())
                    .lastName(employee.getLastName())
                    .bonusCoefficient(employee.getBonusCoefficient()).build());
        } else {
            response.setErrors(errors);
            log.warn("Validation failed errors : {}" , errors);
        }
        log.info("{} is execute!", this.getClass().getSimpleName());
        return response;
    }
}
