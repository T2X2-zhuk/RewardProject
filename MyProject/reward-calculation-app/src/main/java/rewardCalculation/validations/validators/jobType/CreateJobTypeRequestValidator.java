package rewardCalculation.validations.validators.jobType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import rewardCalculation.requests.CommonRequestForEmployeeParameters;
import rewardCalculation.requests.CommonRequestForJobTypeParameters;
import rewardCalculation.util.forError.ValidationError;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForEmployee;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForJobTypesEntity;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateJobTypeRequestValidator {

    private final ValidatorClassWithMethodsForJobTypesEntity validator;

    public List<ValidationError> validate(CommonRequestForJobTypeParameters request){
        log.info("{} start!", this.getClass().getSimpleName());
        List<ValidationError> errors = new ArrayList<>();
        validator.jobTypeMustNotBeEmpty(request.getJobTypeDTO().getJobType()).ifPresent(errors ::add);
        if (errors.isEmpty()){
            validator.suchJobTypesIsDatabase(request.getJobTypeDTO().getJobType()).ifPresent(errors ::add);
        }
        log.info("{} execute!" , this.getClass().getSimpleName());
        return errors;
    }
}
