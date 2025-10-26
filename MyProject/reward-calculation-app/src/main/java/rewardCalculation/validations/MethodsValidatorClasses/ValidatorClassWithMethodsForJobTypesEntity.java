package rewardCalculation.validations.MethodsValidatorClasses;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import rewardCalculation.JPA.repositories.JobTypeEntityRepository;
import rewardCalculation.util.forErrors.ValidationError;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidatorClassWithMethodsForJobTypesEntity {

    private final JobTypeEntityRepository repository;
    private final ValidationErrorFactory errorFactory;

    public Optional<ValidationError> jobTypeIdMustNotBeEmpty(Long jobTypeId){
        if (repository.findById(jobTypeId).isEmpty()){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_FOR_JOB_TYPE_2"));
            log.debug("Error : {}",error);
            return error;
        }
        return Optional.empty();
    }

    public Optional<ValidationError> suchJobTypesIsDatabase(String jobType){
        if (repository.findByJobType(jobType.toUpperCase()).isPresent()){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_FOR_JOB_TYPE_4"));
            log.debug("Error : {}",error);
            return error;
        }
        return Optional.empty();
    }

    public Optional<ValidationError> isSuchJobTypeInDatabase(String jobType){
        if (repository.findByJobType(jobType).isEmpty()){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_FOR_JOB_TYPE_3"));
            log.debug("Error : {}",error);
            return error;
        }
        return Optional.empty();
    }
    public Optional<ValidationError> jobTypeMustNotBeEmpty(String jobType){
        if (isNullOrBlankOrEmpty(jobType)){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_FOR_JOB_TYPE_1"));
            log.debug("Error : {}",error);
            return error;
        }
        return Optional.empty();
    }

    private boolean isNullOrBlankOrEmpty(String parameter) {
        return parameter == null || parameter.isBlank();
    }
}
