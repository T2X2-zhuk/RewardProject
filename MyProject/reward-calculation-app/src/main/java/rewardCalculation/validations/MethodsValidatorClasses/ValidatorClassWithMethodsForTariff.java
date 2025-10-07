package rewardCalculation.validations.MethodsValidatorClasses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.JPA.repositories.TariffRepository;
import rewardCalculation.util.Placeholder;
import rewardCalculation.util.ValidationError;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidatorClassWithMethodsForTariff {

     private final TariffRepository tariffRepository;
     private final ValidationErrorFactory errorFactory;

    public Optional<ValidationError> isSuchTariffIsNull(Long id){
        if (tariffRepository.findById(id).isEmpty()){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_For_Tariff_2"));
            log.debug("Error : {}",error);
            return error;
        }

       return Optional.empty();
    }

    public Optional<ValidationError> amountMustNotBeEmpty(BigDecimal amount){
        if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_For_Tariff_3"));
            log.debug("Error : {}",error);
            return error;
        }
        return Optional.empty();
    }
    public Optional<ValidationError> jobTypeIsEmpty(String jobType){

        if (isNullOrBlankOrEmpty(jobType)){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_For_Tariff_1"));
            log.debug("Error : {}",error);
            return error;
        }
        return Optional.empty();
    }
    public Optional<ValidationError> suchJobTypesIsNotInDatabase(String jobType){
        List<String> jobTypes = tariffRepository.findAllJobTypes();
        if (!jobTypes.isEmpty()){
            if (!jobTypes.contains(jobType.toUpperCase())){
                String joinJobTypes = String.join("," ,jobTypes);
                Placeholder placeholder = new Placeholder("JOB_TYPES",joinJobTypes);
                Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_For_Tariff_4",List.of(placeholder)));
                log.debug("Error : {}",error);
                return error;
            }
        }else {
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_For_Tariff_5"));
            log.debug("Error : {}",error);
            return error;
        }
       return Optional.empty();
    }

    public Optional<ValidationError> suchTariffAlwaysIsInDatabase(String jobType){
        if (tariffRepository.findByJobType(jobType.toUpperCase()).isPresent()){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_For_Tariff_6"));
            log.debug("Error : {}",error);
            return error;
        }
        return Optional.empty();
    }

    private boolean isNullOrBlankOrEmpty(String parameter) {
        return parameter == null || parameter.isBlank() || parameter.isEmpty();
    }
}
