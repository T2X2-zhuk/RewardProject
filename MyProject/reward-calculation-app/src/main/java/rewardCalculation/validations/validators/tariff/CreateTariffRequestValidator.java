package rewardCalculation.validations.validators.tariff;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.requests.CommonRequestForRewardParameters;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForJobTypesEntity;
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
    private final ValidatorClassWithMethodsForJobTypesEntity validatorClassWithMethodsForJobTypesEntity;

    public List<ValidationError> validate(CommonRequestForTariffParameters request){
        log.info("{} start!", this.getClass().getSimpleName());
        List<ValidationError> errors = new ArrayList<>();
        validatorClassWithMethodsForJobTypesEntity.jobTypeMustNotBeEmpty(request.getTariffDTO().getJobType()).ifPresent(errors::add);
        validatorTariff.amountMustNotBeEmpty(request.getTariffDTO().getAmount()).ifPresent(errors::add);
        if (errors.isEmpty()){
            validationV2(request,errors);
        }
        log.info("{} execute!" , this.getClass().getSimpleName());
        return errors;
    }

    private void validationV2(CommonRequestForTariffParameters request, List<ValidationError> errors){
        validatorClassWithMethodsForJobTypesEntity.isSuchJobTypeInDatabase(request.getTariffDTO().getJobType()).ifPresent(errors::add);
        boolean hasJOB_TYPE_3Error = errors.stream()
                .anyMatch(err -> "ERROR_CODE_FOR_JOB_TYPE_3".equals(err.getErrorCode()));
        if (!hasJOB_TYPE_3Error) {
            validatorTariff.suchTariffAlwaysIsInDatabase(request.getTariffDTO().getJobType()).ifPresent(errors::add);
        }
    }
}
