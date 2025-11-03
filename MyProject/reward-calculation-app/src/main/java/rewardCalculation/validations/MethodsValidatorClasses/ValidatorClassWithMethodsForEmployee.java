package rewardCalculation.validations.MethodsValidatorClasses;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.jpa.domain.Employee;
import rewardCalculation.cache.get.GetEmployeeUsingCache;
import rewardCalculation.util.forErrors.ValidationError;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidatorClassWithMethodsForEmployee {

    private final ValidationErrorFactory errorFactory;
    private final GetEmployeeUsingCache getEmployeeUsingCache;

    public Optional<ValidationError> employeeIsNotDatabase(Long id) {

        boolean exists = getEmployeeUsingCache.getAllEmployeesWithCache().stream()
                .anyMatch(employee -> employee.getId().equals(id));

        if (!exists) {

            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_FOR_EMPLOYEE_5"));

            log.debug("Error : {}", error);
            return error;
        }
        return Optional.empty();
    }

    public Optional<ValidationError> firstNameMustNotBeEmpty(String firstName) {

        if (isNullOrBlankOrEmpty(firstName)) {

            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_FOR_EMPLOYEE_1"));

            log.debug("Error : {}", error);
            return error;
        }
        return Optional.empty();
    }

    public Optional<ValidationError> lastNameMustNotBeEmpty(String lastName) {

        if (isNullOrBlankOrEmpty(lastName)) {

            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_FOR_EMPLOYEE_2"));

            log.debug("Error : {}", error);
            return error;
        }
        return Optional.empty();
    }

    public Optional<ValidationError> bonusCoefficientMustNotBeEmpty(BigDecimal bonusCoefficient) {

        if (bonusCoefficient == null || bonusCoefficient.compareTo(BigDecimal.ZERO) == 0) {

            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_FOR_EMPLOYEE_3"));

            log.debug("Error : {}", error);
            return error;
        }
        return Optional.empty();
    }

    public Optional<ValidationError> existInDatabase(String firstName, String lastName, BigDecimal bonusCoefficient) {

        Optional<Employee> existingEmployee = getEmployeeUsingCache.getAllEmployeesWithCache().stream()
                .filter(e -> e.getFirstName().equals(firstName)
                        && e.getLastName().equals(lastName)
                        && e.getBonusCoefficient().compareTo(bonusCoefficient) == 0)
                .findFirst();

        if (existingEmployee.isPresent()) {

            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_FOR_EMPLOYEE_4"));

            log.debug("Error : {}", error);
            return error;
        }
        return Optional.empty();
    }
    private boolean isNullOrBlankOrEmpty(String parameter) {
        return parameter == null || parameter.isBlank();
    }
}
