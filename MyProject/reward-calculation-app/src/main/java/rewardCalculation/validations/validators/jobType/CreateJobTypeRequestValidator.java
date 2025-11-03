package rewardCalculation.validations.validators.jobType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import rewardCalculation.requests.CommonRequestForJobTypeParameters;
import rewardCalculation.util.forErrors.ValidationError;
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

        log.info("{} execute! Errors : {}" , this.getClass().getSimpleName(),errors);
        return errors;
    }
}
