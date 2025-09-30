package rewardCalculation.validations.validators.tariff;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForTariff;
import rewardCalculation.requests.CommonRequestForTariffParameters;

import rewardCalculation.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@ToString
@Slf4j
public class CreateTariffRequestValidator {

    private final ValidatorClassWithMethodsForTariff validatorTariff;

    public List<ValidationError> validate(CommonRequestForTariffParameters request){
        log.info("{} start!", this);
        List<ValidationError> errors = new ArrayList<>();
        validatorTariff.jobTypeIsEmpty(request.getTariffDTO().getJobType()).ifPresent(errors::add);
        validatorTariff.amountMustNotBeEmpty(request.getTariffDTO().getAmount()).ifPresent(errors::add);
        if (errors.isEmpty()){
            validatorTariff.suchTariffAlwaysIsInDatabase(request.getTariffDTO().getJobType()).ifPresent(errors::add);
        }
        log.info("{} execute!" , this);
        return errors;
    }
}
