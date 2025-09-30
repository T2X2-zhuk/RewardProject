package rewardCalculation.validations.validators.employee;
import lombok.RequiredArgsConstructor;
import rewardCalculation.requests.CommonRequestForEmployeeParameters;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForEmployee;
import rewardCalculation.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateEmployeeRequestValidator {

    private final ValidatorClassWithMethodsForEmployee validator;

    public List<ValidationError> validate(CommonRequestForEmployeeParameters request){
        List<ValidationError> errors = new ArrayList<>();
        validator.firstNameMustNotBeEmpty(request.getEmployeeDTO().getFirstName()).ifPresent(errors ::add);
        validator.lastNameMustNotBeEmpty(request.getEmployeeDTO().getLastName()).ifPresent(errors ::add);
        validator.bonusCoefficientMustNotBeEmpty(request.getEmployeeDTO().getBonusCoefficient()).ifPresent(errors ::add);
        return errors;
    }
}
