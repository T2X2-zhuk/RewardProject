package rewardCalculation.validations.validators.employee;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.requests.CommonRequestForEmployeeParameters;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForEmployee;
import rewardCalculation.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@ToString
@Slf4j
public class CreateEmployeeRequestValidator {

    private final ValidatorClassWithMethodsForEmployee validator;

    public List<ValidationError> validate(CommonRequestForEmployeeParameters request){
        log.info("{} start!", this);
        List<ValidationError> errors = new ArrayList<>();
        validator.firstNameMustNotBeEmpty(request.getEmployeeDTO().getFirstName()).ifPresent(errors ::add);
        validator.lastNameMustNotBeEmpty(request.getEmployeeDTO().getLastName()).ifPresent(errors ::add);
        validator.bonusCoefficientMustNotBeEmpty(request.getEmployeeDTO().getBonusCoefficient()).ifPresent(errors ::add);
        log.info("{} execute!" , this);
        return errors;
    }
}
