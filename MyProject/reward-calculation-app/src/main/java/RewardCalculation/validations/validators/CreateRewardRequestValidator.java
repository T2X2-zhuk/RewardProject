package RewardCalculation.validations.validators;

import RewardCalculation.requests.CreateRewardRequest;
import RewardCalculation.util.ValidationError;
import RewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForReward;
import RewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForTariff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CreateRewardRequestValidator {

    @Autowired private ValidatorClassWithMethodsForReward validator;
    @Autowired private ValidatorClassWithMethodsForTariff tariffValidator;

    public List<ValidationError> validate(CreateRewardRequest request){
        List<ValidationError> errors = new ArrayList<>();
        validator.employeeIdMustNotBeEmpty(request.getEmployeeId()).ifPresent(errors ::add);
        tariffValidator.jobTypeIsEmpty(request.getJobType()).ifPresent(errors ::add);
        if (errors.isEmpty()){
            tariffValidator.suchJobTypesIsNotInDatabase(request.getJobType()).ifPresent(errors::add);
        }
        return errors;
    }
}
