package rewardCalculation.validations.validators.reward;

import lombok.RequiredArgsConstructor;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForReward;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForTariff;
import rewardCalculation.requests.CommonRequestForRewardParameters;
import rewardCalculation.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateRewardRequestValidator {

    private final ValidatorClassWithMethodsForReward validator;
    private final ValidatorClassWithMethodsForTariff tariffValidator;

    public List<ValidationError> validate(CommonRequestForRewardParameters request){
        List<ValidationError> errors = new ArrayList<>();
        validator.employeeIdMustNotBeEmpty(request.getRewardDTO().getEmployeeId()).ifPresent(errors ::add);
        tariffValidator.jobTypeIsEmpty(request.getRewardDTO().getJobType()).ifPresent(errors ::add);
        if (errors.isEmpty()){
            tariffValidator.suchJobTypesIsNotInDatabase(request.getRewardDTO().getJobType()).ifPresent(errors::add);
        }
        return errors;
    }
}
