package rewardCalculation.validations.validators.employee;
import lombok.RequiredArgsConstructor;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForEmployee;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForReward;
import rewardCalculation.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetEmployeeValidator {

    private final ValidatorClassWithMethodsForReward validator;
    private final ValidatorClassWithMethodsForEmployee forEmployee;

    public List<ValidationError> validate(Long id){
        List<ValidationError> errors = new ArrayList<>();
        forEmployee.employeeIsNotDatabase(id).ifPresent(errors::add);
        return errors;
    }
}
