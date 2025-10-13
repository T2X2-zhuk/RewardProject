package rewardCalculation.validations.validators.reward;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForReward;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForTariff;
import rewardCalculation.requests.CommonRequestForRewardParameters;
import rewardCalculation.util.forError.ValidationError;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateRewardRequestValidator {

    private final ValidatorClassWithMethodsForReward validator;
    private final ValidatorClassWithMethodsForTariff tariffValidator;

    public List<ValidationError> validate(CommonRequestForRewardParameters request){
        log.info("{} start!", this.getClass().getSimpleName());
        List<ValidationError> errors = new ArrayList<>();
        validator.employeeIdMustNotBeEmpty(request.getRewardDTO().getEmployeeId()).ifPresent(errors ::add);
        tariffValidator.jobTypeIsEmpty(request.getRewardDTO().getJobType()).ifPresent(errors ::add);
        if (errors.isEmpty()){
            tariffValidator.suchJobTypesIsNotInDatabase(request.getRewardDTO().getJobType()).ifPresent(errors::add);
            validator.suchRewardIsDatabase(request.getRewardDTO().getEmployeeId(), request.getRewardDTO().getJobType()).ifPresent(errors::add);
        }
        log.info("{} execute!" , this.getClass().getSimpleName());
        return errors;
    }
}
