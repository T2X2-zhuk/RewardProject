package rewardCalculation.validations.validators.reward;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.JPA.domain.Employee;
import rewardCalculation.JPA.domain.Reward;
import rewardCalculation.util.forError.ValidationError;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForEmployee;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForReward;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RewardCalculationValidator {

    private final ValidatorClassWithMethodsForEmployee methodsForEmployee;
    private final ValidatorClassWithMethodsForReward methodsForReward;

    public List<ValidationError> validate(List<Employee> employees,List<Reward> rewardList){
        log.info("{} start!", this.getClass().getSimpleName());
        List<ValidationError> errors = new ArrayList<>();
        methodsForEmployee.listEmployeeIsEmpty(employees).ifPresent(errors::add);
        if (errors.isEmpty()){
            methodsForReward.isRewardsForThisEmployees(rewardList).ifPresent(errors::add);
            methodsForReward.isNotASingleRewardWithStatusUNPAID(rewardList).ifPresent(errors::add);
        }
        log.info("{} execute!", this.getClass().getSimpleName());
        return errors;
    }
}
