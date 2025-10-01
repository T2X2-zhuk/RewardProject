package rewardCalculation.validations.MethodsValidatorClasses;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.JPA.domain.Employee;
import rewardCalculation.JPA.repositories.EmployeeRepository;
import rewardCalculation.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidatorClassWithMethodsForEmployee {

     private final EmployeeRepository employeeRepository;
     private final ValidationErrorFactory errorFactory;

    public Optional<ValidationError> employeeIsNotDatabase(Long id){
        if (employeeRepository.findById(id).isEmpty()){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_For_Employee_5"));
            log.debug("Error : {}",error);
            return error;
        }
      return Optional.empty();
    }

    public Optional<ValidationError> firstNameMustNotBeEmpty(String firstName){
        if (isNullOrBlankOrEmpty(firstName)){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_For_Employee_1"));
            log.debug("Error : {}",error);
            return error;
        }
        return Optional.empty();
    }

    public Optional<ValidationError> lastNameMustNotBeEmpty(String lastName){
        if (isNullOrBlankOrEmpty(lastName)){
            Optional<ValidationError> error  = Optional.of(errorFactory.buildError("ERROR_CODE_For_Employee_2"));
            log.debug("Error : {}",error);
            return error;
        }
        return Optional.empty();
    }

    public Optional<ValidationError> bonusCoefficientMustNotBeEmpty(double bonusCoefficient){

        if (bonusCoefficient == 0.0){
            Optional<ValidationError> error  = Optional.of(errorFactory.buildError("ERROR_CODE_For_Employee_3"));
            log.debug("Error : {}",error);
            return error;
        }
        return Optional.empty();
    }

    public Optional<ValidationError> duplicateEmployeeError(String firstName,String lastName){

        if (employeeRepository.findByFirstNameAndLastName(firstName,lastName).isPresent()){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_For_Employee_4"));
            log.debug("Error : {}",error);
            return error;
        }

        return Optional.empty();
    }

    public Optional<ValidationError> listEmployeeIsEmpty(List<Employee> employees){
        if (employees.isEmpty()){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_For_Employee_6"));
            log.debug("Error : {}",error);
            return error;
        }

        return Optional.empty();
    }
    private boolean isNullOrBlankOrEmpty(String parameter) {
        return parameter == null || parameter.isBlank() || parameter.isEmpty();
    }
}
