package rewardCalculation.validations.validators.employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.requests.CommonRequestForEmployeeParameters;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForEmployee;
import rewardCalculation.util.forErrors.ValidationError;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateEmployeeRequestValidator {

    private final ValidatorClassWithMethodsForEmployee validator;

    public List<ValidationError> validate(CommonRequestForEmployeeParameters request){
        log.info("{} start!", this.getClass().getSimpleName());

        List<ValidationError> errors = new ArrayList<>();

        validator.firstNameMustNotBeEmpty(request.getEmployeeDTO().getFirstName()).ifPresent(errors ::add);
        validator.lastNameMustNotBeEmpty(request.getEmployeeDTO().getLastName()).ifPresent(errors ::add);
        validator.bonusCoefficientMustNotBeEmpty(request.getEmployeeDTO().getBonusCoefficient()).ifPresent(errors ::add);

        if (errors.isEmpty()){
            validator.existInDatabase(request.getEmployeeDTO().getFirstName(),request.getEmployeeDTO().getLastName(),request.getEmployeeDTO().getBonusCoefficient()).ifPresent(errors ::add);
        }

        log.info("{} execute! Errors : {}" , this.getClass().getSimpleName(),errors);
        return errors;
    }
}
