package rewardCalculation.validations.validators.tariff;

import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForTariff;
import rewardCalculation.requests.CommonRequestForTariffParameters;

import rewardCalculation.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CreateTariffRequestValidator {

    @Autowired private ValidatorClassWithMethodsForTariff validatorTariff;

    public List<ValidationError> validate(CommonRequestForTariffParameters request){
        List<ValidationError> errors = new ArrayList<>();
        validatorTariff.jobTypeIsEmpty(request.getTariffDTO().getJobType()).ifPresent(errors::add);
        validatorTariff.amountMustNotBeEmpty(request.getTariffDTO().getAmount()).ifPresent(errors::add);
        if (errors.isEmpty()){
            validatorTariff.suchTariffAlwaysIsInDatabase(request.getTariffDTO().getJobType()).ifPresent(errors::add);
        }
        return errors;
    }
}
