package rewardCalculation.validations.validators.tariff;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForTariff;
import rewardCalculation.requests.CommonRequestForTariffParameters;

import rewardCalculation.util.forError.ValidationError;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateTariffRequestValidator {

    private final ValidatorClassWithMethodsForTariff validatorTariff;

    public List<ValidationError> validate(CommonRequestForTariffParameters request){
        log.info("{} start!", this.getClass().getSimpleName());
        List<ValidationError> errors = new ArrayList<>();
        validatorTariff.jobTypeIsEmpty(request.getTariffDTO().getJobType()).ifPresent(errors::add);
        validatorTariff.amountMustNotBeEmpty(request.getTariffDTO().getAmount()).ifPresent(errors::add);
        if (errors.isEmpty()){
            validatorTariff.suchTariffAlwaysIsInDatabase(request.getTariffDTO().getJobType()).ifPresent(errors::add);
        }
        log.info("{} execute!" , this.getClass().getSimpleName());
        return errors;
    }
}
