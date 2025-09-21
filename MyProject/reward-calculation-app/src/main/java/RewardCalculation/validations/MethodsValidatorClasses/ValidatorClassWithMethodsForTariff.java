package RewardCalculation.validations.MethodsValidatorClasses;
import RewardCalculation.JPA.repositories.TariffRepository;
import RewardCalculation.util.Placeholder;
import RewardCalculation.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ValidatorClassWithMethodsForTariff {

    @Autowired private TariffRepository tariffRepository;
    @Autowired private ValidationErrorFactory errorFactory;

    public Optional<ValidationError> isSuchTariffIsNull(Long id){
        return (id == null || tariffRepository.findById(id).isEmpty())
                ? Optional.of(errorFactory.buildError("ERROR_CODE_10"))
                : Optional.empty();
    }

    public Optional<ValidationError> amountMustNotBeEmpty(double amount){
        return (amount == 0.0)
                ? Optional.of(errorFactory.buildError("ERROR_CODE_11"))
                : Optional.empty();
    }
    public Optional<ValidationError> jobTypeIsEmpty(String jobType){
        return (isNullOrBlankOrEmpty(jobType))
                ? Optional.of(errorFactory.buildError("ERROR_CODE_6"))
                : Optional.empty();
    }
    public Optional<ValidationError> suchJobTypesIsNotInDatabase(String jobType){
        List<String> jobTypes = tariffRepository.findAllJobTypes();
        if (!jobTypes.contains(jobType)){
            if (!jobTypes.isEmpty()){
                String joinJobTypes = String.join("," ,jobTypes);
                Placeholder placeholder = new Placeholder("JOB_TYPES",joinJobTypes);
                return Optional.of(errorFactory.buildError("ERROR_CODE_12",List.of(placeholder)));
            }else {
                return Optional.of(errorFactory.buildError("ERROR_CODE_12"));
            }
        }
        return Optional.empty();
    }

    public Optional<ValidationError> suchTariffAlwaysIsInDatabase(String jobType){
        return (tariffRepository.findByJobType(jobType).isPresent())
                ? Optional.of(errorFactory.buildError("ERROR_CODE_14"))
                : Optional.empty();
    }

    private boolean isNullOrBlankOrEmpty(String parameter) {
        return parameter == null || parameter.isBlank() || parameter.isEmpty();
    }
}
