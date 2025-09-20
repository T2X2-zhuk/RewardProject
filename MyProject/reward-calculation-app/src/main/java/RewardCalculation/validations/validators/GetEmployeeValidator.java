package RewardCalculation.validations.validators;
import RewardCalculation.JPA.repositories.EmployeeRepository;
import RewardCalculation.util.ValidationError;
import RewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForEmployee;
import RewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForReward;
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
