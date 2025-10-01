package rewardCalculation.validations.validators.tariff;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForTariff;
import rewardCalculation.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetTariffValidator {


    private final ValidatorClassWithMethodsForTariff validatorClassWithMethodsForTariff;

    public List<ValidationError> validate(Long id){
        log.info("{} start!", this.getClass().getSimpleName());
        List<ValidationError> errors = new ArrayList<>();
        validatorClassWithMethodsForTariff.isSuchTariffIsNull(id).ifPresent(errors::add);
        log.info("{} execute!" , this.getClass().getSimpleName());
        return errors;
    }
}
