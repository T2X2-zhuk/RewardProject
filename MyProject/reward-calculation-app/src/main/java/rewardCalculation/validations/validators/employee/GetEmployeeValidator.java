package rewardCalculation.validations.validators.employee;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForEmployee;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForReward;
import rewardCalculation.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetEmployeeValidator {

    private final ValidatorClassWithMethodsForReward validator;
    private final ValidatorClassWithMethodsForEmployee forEmployee;

    public List<ValidationError> validate(Long id){
        log.info("{} start!", this.getClass().getSimpleName());
        List<ValidationError> errors = new ArrayList<>();
        forEmployee.employeeIsNotDatabase(id).ifPresent(errors::add);
        log.info("{} execute!" , this.getClass().getSimpleName());
        return errors;
    }
}
