package rewardCalculation.validations.MethodsValidatorClasses;

import lombok.RequiredArgsConstructor;
import rewardCalculation.JPA.domain.Employee;
import rewardCalculation.JPA.repositories.EmployeeRepository;
import rewardCalculation.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ValidatorClassWithMethodsForEmployee {

     private final EmployeeRepository employeeRepository;
     private final ValidationErrorFactory errorFactory;

    public Optional<ValidationError> employeeIsNotDatabase(Long id){
      return (employeeRepository.findById(id).isEmpty())
      ? Optional.of(errorFactory.buildError("ERROR_CODE_For_Employee_5"))
      : Optional.empty();
    }

    public Optional<ValidationError> firstNameMustNotBeEmpty(String firstName){
        return (isNullOrBlankOrEmpty(firstName))
                ? Optional.of(errorFactory.buildError("ERROR_CODE_For_Employee_1"))
                : Optional.empty();
    }

    public Optional<ValidationError> lastNameMustNotBeEmpty(String lastName){
        return (isNullOrBlankOrEmpty(lastName))
                ? Optional.of(errorFactory.buildError("ERROR_CODE_For_Employee_2"))
                : Optional.empty();
    }

    public Optional<ValidationError> bonusCoefficientMustNotBeEmpty(double bonusCoefficient){
        return (bonusCoefficient == 0.0)
                ? Optional.of(errorFactory.buildError("ERROR_CODE_For_Employee_3"))
                : Optional.empty();
    }

    public Optional<ValidationError> duplicateEmployeeError(String firstName,String lastName){
        return (employeeRepository.findByFirstNameAndLastName(firstName,lastName).isPresent())
                ? Optional.of(errorFactory.buildError("ERROR_CODE_For_Employee_4"))
                : Optional.empty();
    }

    public Optional<ValidationError> listEmployeeIsEmpty(List<Employee> employees){
        return (employees.isEmpty())
                ? Optional.of(errorFactory.buildError("ERROR_CODE_For_Employee_6"))
                : Optional.empty();
    }
    private boolean isNullOrBlankOrEmpty(String parameter) {
        return parameter == null || parameter.isBlank() || parameter.isEmpty();
    }
}
