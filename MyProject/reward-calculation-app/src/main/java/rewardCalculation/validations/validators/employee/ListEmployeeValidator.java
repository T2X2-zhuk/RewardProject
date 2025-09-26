package rewardCalculation.validations.validators.employee;
import rewardCalculation.JPA.domain.Employee;
import rewardCalculation.util.ValidationError;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForEmployee;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForReward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ListEmployeeValidator {

    @Autowired private ValidatorClassWithMethodsForEmployee methodsForEmployee;
    @Autowired private ValidatorClassWithMethodsForReward methodsForReward;
    public List<ValidationError> validate(List<Employee> employees){
        List<ValidationError> errors = new ArrayList<>();
        methodsForEmployee.listEmployeeIsEmpty(employees).ifPresent(errors::add);
        if (errors.isEmpty()){
            methodsForReward.isRewardsForThisEmployees(employees).ifPresent(errors::add);
        }
        return errors;
    }
}
