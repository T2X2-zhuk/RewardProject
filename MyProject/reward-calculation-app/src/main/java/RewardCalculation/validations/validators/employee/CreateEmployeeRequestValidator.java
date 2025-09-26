package RewardCalculation.validations.validators.employee;
import RewardCalculation.requests.CommonRequestForEmployeeParameters;
import RewardCalculation.util.ValidationError;
import RewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CreateEmployeeRequestValidator {

    @Autowired private ValidatorClassWithMethodsForEmployee validator;

    public List<ValidationError> validate(CommonRequestForEmployeeParameters request){
        List<ValidationError> errors = new ArrayList<>();
        validator.firstNameMustNotBeEmpty(request.getEmployeeDTO().getFirstName()).ifPresent(errors ::add);
        validator.lastNameMustNotBeEmpty(request.getEmployeeDTO().getLastName()).ifPresent(errors ::add);
        validator.bonusCoefficientMustNotBeEmpty(request.getEmployeeDTO().getBonusCoefficient()).ifPresent(errors ::add);
        return errors;
    }
}
