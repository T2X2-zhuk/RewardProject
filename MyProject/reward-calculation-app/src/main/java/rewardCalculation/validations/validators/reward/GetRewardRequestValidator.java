package rewardCalculation.validations.validators.reward;

import lombok.RequiredArgsConstructor;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForReward;
import rewardCalculation.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetRewardRequestValidator {

    private final ValidatorClassWithMethodsForReward validator;

    public List<ValidationError> validate(Long id){
        List<ValidationError> errors = new ArrayList<>();
        validator.isSuchRewardById(id).ifPresent(errors::add);
        return errors;
    }
}
