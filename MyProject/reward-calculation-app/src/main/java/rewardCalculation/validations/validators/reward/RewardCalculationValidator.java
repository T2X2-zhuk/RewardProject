package rewardCalculation.validations.validators.reward;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.JPA.domain.Reward;
import rewardCalculation.util.forErrors.ValidationError;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForReward;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RewardCalculationValidator {

    private final ValidatorClassWithMethodsForReward methodsForReward;

    public List<ValidationError> validate(List<Reward> rewardList){
        log.info("{} start!", this.getClass().getSimpleName());
        List<ValidationError> errors = new ArrayList<>();
        methodsForReward.isRewardsForThisEmployees(rewardList).ifPresent(errors::add);
        log.info("{} execute!", this.getClass().getSimpleName());
        return errors;
    }
}
