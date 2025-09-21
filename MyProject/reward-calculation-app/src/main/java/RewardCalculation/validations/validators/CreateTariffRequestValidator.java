package RewardCalculation.validations.validators;

import RewardCalculation.requests.CreateRewardRequest;
import RewardCalculation.requests.CreateTariffRequest;
import RewardCalculation.util.ValidationError;
import RewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForTariff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CreateTariffRequestValidator {

    @Autowired private ValidatorClassWithMethodsForTariff validatorTariff;

    public List<ValidationError> validate(CreateTariffRequest request){
        List<ValidationError> errors = new ArrayList<>();
        validatorTariff.jobTypeIsEmpty(request.getTariffDTO().getJobType()).ifPresent(errors::add);
        validatorTariff.amountMustNotBeEmpty(request.getTariffDTO().getAmount()).ifPresent(errors::add);
        if (errors.isEmpty()){
            validatorTariff.suchTariffAlwaysIsInDatabase(request.getTariffDTO().getJobType()).ifPresent(errors::add);
        }
        return errors;
    }
}
