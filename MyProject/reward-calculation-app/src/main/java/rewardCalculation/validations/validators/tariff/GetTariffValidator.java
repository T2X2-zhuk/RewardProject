package rewardCalculation.validations.validators.tariff;
import lombok.RequiredArgsConstructor;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForTariff;
import rewardCalculation.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetTariffValidator {


    private final ValidatorClassWithMethodsForTariff validatorClassWithMethodsForTariff;

    public List<ValidationError> validate(Long id){
        List<ValidationError> errors = new ArrayList<>();
        validatorClassWithMethodsForTariff.isSuchTariffIsNull(id).ifPresent(errors::add);
        return errors;
    }
}
