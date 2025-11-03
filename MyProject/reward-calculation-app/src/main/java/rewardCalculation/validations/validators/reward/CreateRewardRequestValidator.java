package rewardCalculation.validations.validators.reward;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForEmployee;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForJobTypesEntity;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForReward;
import rewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForTariff;
import rewardCalculation.requests.CommonRequestForRewardParameters;
import rewardCalculation.util.forErrors.ValidationError;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateRewardRequestValidator {

    private final ValidatorClassWithMethodsForReward validator;
    private final ValidatorClassWithMethodsForTariff tariffValidator;
    private final ValidatorClassWithMethodsForEmployee employee;
    private final ValidatorClassWithMethodsForJobTypesEntity jobTypesEntity;

    public List<ValidationError> validate(CommonRequestForRewardParameters request){
        log.info("{} start!", this.getClass().getSimpleName());

        List<ValidationError> errors = new ArrayList<>();

        validator.employeeIdMustNotBeEmpty(request.getRewardDTO().getEmployeeId()).ifPresent(errors ::add);
        jobTypesEntity.jobTypeMustNotBeEmpty(request.getRewardDTO().getJobType()).ifPresent(errors ::add);

        if (errors.isEmpty()){
            validationV2(request,errors);
        }

        log.info("{} execute! Errors : {}" , this.getClass().getSimpleName(),errors);
        return errors;
    }

    private void validationV2(CommonRequestForRewardParameters request,List<ValidationError> errors){

        employee.employeeIsNotDatabase(request.getRewardDTO().getEmployeeId()).ifPresent(errors::add);
        tariffValidator.suchTariffWithSuchJobTypeIsNotInDatabase(request.getRewardDTO().getJobType()).ifPresent(errors::add);

        boolean hasTariff5Error = errors.stream()
                .anyMatch(err -> "ERROR_CODE_FOR_TARIFF_5".equals(err.getErrorCode()));

        if (!hasTariff5Error) {
            validator.suchRewardIsDatabase(request.getRewardDTO().getEmployeeId(),
                            request.getRewardDTO().getJobType())
                    .ifPresent(errors::add);
        }
    }
}
