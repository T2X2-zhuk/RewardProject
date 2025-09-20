package RewardCalculation.validations.validators;

import RewardCalculation.JPA.repositories.EmployeeRepository;
import RewardCalculation.requests.CreateEmployeeRequest;
import RewardCalculation.util.ValidationError;
import RewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CreateEmployeeRequestValidator {

    @Autowired private ValidatorClassWithMethodsForEmployee validator;

    public List<ValidationError> validate(CreateEmployeeRequest request){
        List<ValidationError> errors = new ArrayList<>();
        validator.firstNameMustNotBeEmpty(request.getFirstName()).ifPresent(errors ::add);
        validator.lastNameMustNotBeEmpty(request.getLastName()).ifPresent(errors ::add);
        validator.bonusCoefficientMustNotBeEmpty(request.getBonusCoefficient()).ifPresent(errors ::add);
        if (errors.isEmpty()){
            validator.duplicateEmployeeError(request.getFirstName(),request.getLastName()).ifPresent(errors ::add);
        }
        return errors;
    }
}
