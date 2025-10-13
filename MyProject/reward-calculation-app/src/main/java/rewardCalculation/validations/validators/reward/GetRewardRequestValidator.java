package rewardCalculation.validations.validators.reward;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForReward;
import rewardCalculation.util.forError.ValidationError;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetRewardRequestValidator {

    private final ValidatorClassWithMethodsForReward validator;

    public List<ValidationError> validate(Long id){
        log.info("{} start!", this.getClass().getSimpleName());
        List<ValidationError> errors = new ArrayList<>();
        validator.isSuchRewardById(id).ifPresent(errors::add);
        log.info("{} execute!" , this.getClass().getSimpleName());
        return errors;
    }
}
