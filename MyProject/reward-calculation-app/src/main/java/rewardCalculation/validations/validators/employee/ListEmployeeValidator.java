package rewardCalculation.validations.validators.employee;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.JPA.domain.Employee;
import rewardCalculation.util.ValidationError;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForEmployee;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForReward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@ToString
@Slf4j
public class ListEmployeeValidator {

    private final ValidatorClassWithMethodsForEmployee methodsForEmployee;
    private final ValidatorClassWithMethodsForReward methodsForReward;

    public List<ValidationError> validate(List<Employee> employees){
        log.info("{} start!", this);
        List<ValidationError> errors = new ArrayList<>();
        methodsForEmployee.listEmployeeIsEmpty(employees).ifPresent(errors::add);
        if (errors.isEmpty()){
            methodsForReward.isRewardsForThisEmployees(employees).ifPresent(errors::add);
        }
        log.info("{} execute!", this);
        return errors;
    }
}
