package rewardCalculation.validations.validators.employee;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForEmployee;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForReward;
import rewardCalculation.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GetEmployeeValidator {

    @Autowired private ValidatorClassWithMethodsForReward validator;
    @Autowired private ValidatorClassWithMethodsForEmployee forEmployee;

    public List<ValidationError> validate(Long id){
        List<ValidationError> errors = new ArrayList<>();
        validator.employeeIdMustNotBeEmpty(id).ifPresent(errors::add);
        if (errors.isEmpty()){
            forEmployee.employeeIsNotDatabase(id).ifPresent(errors::add);
        }
        return errors;
    }
}
