package rewardCalculation.validations.validators.employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForEmployee;
import rewardCalculation.util.forErrors.ValidationError;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetEmployeeValidator {

    private final ValidatorClassWithMethodsForEmployee forEmployee;

    public List<ValidationError> validate(Long id){
        log.info("{} start!", this.getClass().getSimpleName());

        List<ValidationError> errors = new ArrayList<>();

        forEmployee.employeeIsNotDatabase(id).ifPresent(errors::add);

        log.info("{} execute! Errors : {}" , this.getClass().getSimpleName(),errors);
        return errors;
    }
}
