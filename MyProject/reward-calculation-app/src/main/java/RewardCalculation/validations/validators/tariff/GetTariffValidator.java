package RewardCalculation.validations.validators.tariff;
import RewardCalculation.JPA.repositories.TariffRepository;
import RewardCalculation.util.ValidationError;
import RewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForEmployee;
import RewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForReward;
import RewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForTariff;
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
