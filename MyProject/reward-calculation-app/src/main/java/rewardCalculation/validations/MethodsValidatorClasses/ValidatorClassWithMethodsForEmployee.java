package rewardCalculation.validations.MethodsValidatorClasses;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.JPA.domain.Employee;
import rewardCalculation.JPA.repositories.EmployeeRepository;
import rewardCalculation.cacheConfig.GetEmployeeUsingCache;
import rewardCalculation.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidatorClassWithMethodsForEmployee {

    private final ValidationErrorFactory errorFactory;
    private final GetEmployeeUsingCache getEmployeeUsingCache;

    public Optional<ValidationError> employeeIsNotDatabase(Long id){
        boolean exists = getEmployeeUsingCache.getAllEmployeesWithCache().stream()
                .anyMatch(employee -> employee.getId().equals(id));
        if (!exists){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_FOR_EMPLOYEE_5"));
            log.debug("Error : {}",error);
            return error;
        }
      return Optional.empty();
    }

    public Optional<ValidationError> firstNameMustNotBeEmpty(String firstName){
        if (isNullOrBlankOrEmpty(firstName)){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_FOR_EMPLOYEE_1"));
            log.debug("Error : {}",error);
            return error;
        }
        return Optional.empty();
    }

    public Optional<ValidationError> lastNameMustNotBeEmpty(String lastName){
        if (isNullOrBlankOrEmpty(lastName)){
            Optional<ValidationError> error  = Optional.of(errorFactory.buildError("ERROR_CODE_FOR_EMPLOYEE_2"));
            log.debug("Error : {}",error);
            return error;
        }
        return Optional.empty();
    }

    public Optional<ValidationError> bonusCoefficientMustNotBeEmpty(BigDecimal bonusCoefficient){
        if (bonusCoefficient == null || bonusCoefficient.compareTo(BigDecimal.ZERO) == 0){
            Optional<ValidationError> error  = Optional.of(errorFactory.buildError("ERROR_CODE_FOR_EMPLOYEE_3"));
            log.debug("Error : {}",error);
            return error;
        }
        return Optional.empty();
    }

    public Optional<ValidationError> listEmployeeIsEmpty(List<Employee> employees){
        if (employees.isEmpty()){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_FOR_EMPLOYEE_6"));
            log.debug("Error : {}",error);
            return error;
        }
        return Optional.empty();
    }
    private boolean isNullOrBlankOrEmpty(String parameter) {
        return parameter == null || parameter.isBlank();
    }
}
