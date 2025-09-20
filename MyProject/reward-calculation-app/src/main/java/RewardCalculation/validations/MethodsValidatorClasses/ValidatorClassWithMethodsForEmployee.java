package RewardCalculation.validations.MethodsValidatorClasses;

import RewardCalculation.JPA.domain.Employee;
import RewardCalculation.JPA.repositories.EmployeeRepository;
import RewardCalculation.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ValidatorClassWithMethodsForEmployee {

    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private ValidationErrorFactory errorFactory;

    public Optional<ValidationError> employeeIsNotDatabase(Long id){
      return (employeeRepository.findById(id).isEmpty())
      ? Optional.of(errorFactory.buildError("ERROR_CODE_5"))
      : Optional.empty();
    }

    public Optional<ValidationError> firstNameMustNotBeEmpty(String firstName){
        return (isNullOrBlankOrEmpty(firstName))
                ? Optional.of(errorFactory.buildError("ERROR_CODE_1"))
                : Optional.empty();
    }

    public Optional<ValidationError> lastNameMustNotBeEmpty(String lastName){
        return (isNullOrBlankOrEmpty(lastName))
                ? Optional.of(errorFactory.buildError("ERROR_CODE_2"))
                : Optional.empty();
    }

    public Optional<ValidationError> bonusCoefficientMustNotBeEmpty(double bonusCoefficient){
        return (bonusCoefficient == 0.0)
                ? Optional.of(errorFactory.buildError("ERROR_CODE_3"))
                : Optional.empty();
    }

    public Optional<ValidationError> duplicateEmployeeError(String firstName,String lastName){
        return (employeeRepository.findByFirstNameAndLastName(firstName,lastName).isPresent())
                ? Optional.of(errorFactory.buildError("ERROR_CODE_4"))
                : Optional.empty();
    }

    public Optional<ValidationError> listEmployeeIsEmpty(List<Employee> employees){
        return (employees.isEmpty())
                ? Optional.of(errorFactory.buildError("ERROR_CODE_13"))
                : Optional.empty();
    }
    private boolean isNullOrBlankOrEmpty(String parameter) {
        return parameter == null || parameter.isBlank() || parameter.isEmpty();
    }
}
