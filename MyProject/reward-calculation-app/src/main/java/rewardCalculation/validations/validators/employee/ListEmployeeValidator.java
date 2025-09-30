package rewardCalculation.validations.validators.employee;
import lombok.RequiredArgsConstructor;
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
public class ListEmployeeValidator {

    private final ValidatorClassWithMethodsForEmployee methodsForEmployee;
    private final ValidatorClassWithMethodsForReward methodsForReward;
    public List<ValidationError> validate(List<Employee> employees){
        List<ValidationError> errors = new ArrayList<>();
        methodsForEmployee.listEmployeeIsEmpty(employees).ifPresent(errors::add);
        if (errors.isEmpty()){
            methodsForReward.isRewardsForThisEmployees(employees).ifPresent(errors::add);
        }
        return errors;
    }
}
