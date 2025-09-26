package rewardCalculation.validations.validators.tariff;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForTariff;
import rewardCalculation.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GetTariffValidator {


    @Autowired private ValidatorClassWithMethodsForTariff validatorClassWithMethodsForTariff;

    public List<ValidationError> validate(Long id){
        List<ValidationError> errors = new ArrayList<>();
        validatorClassWithMethodsForTariff.isSuchTariffIsNull(id).ifPresent(errors::add);
        return errors;
    }
}
