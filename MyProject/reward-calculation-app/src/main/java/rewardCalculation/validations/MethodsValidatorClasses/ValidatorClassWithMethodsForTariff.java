package rewardCalculation.validations.MethodsValidatorClasses;
import lombok.RequiredArgsConstructor;
import rewardCalculation.JPA.repositories.TariffRepository;
import rewardCalculation.util.Placeholder;
import rewardCalculation.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ValidatorClassWithMethodsForTariff {

     private final TariffRepository tariffRepository;
     private final ValidationErrorFactory errorFactory;

    public Optional<ValidationError> isSuchTariffIsNull(Long id){
        return (tariffRepository.findById(id).isEmpty())
                ? Optional.of(errorFactory.buildError("ERROR_CODE_For_Tariff_2"))
                : Optional.empty();
    }

    public Optional<ValidationError> amountMustNotBeEmpty(double amount){
        return (amount == 0.0)
                ? Optional.of(errorFactory.buildError("ERROR_CODE_For_Tariff_3"))
                : Optional.empty();
    }
    public Optional<ValidationError> jobTypeIsEmpty(String jobType){
        return (isNullOrBlankOrEmpty(jobType))
                ? Optional.of(errorFactory.buildError("ERROR_CODE_For_Tariff_1"))
                : Optional.empty();
    }
    public Optional<ValidationError> suchJobTypesIsNotInDatabase(String jobType){
        List<String> jobTypes = tariffRepository.findAllJobTypes();
        if (!jobTypes.isEmpty()){
            if (!jobTypes.contains(jobType)){
                String joinJobTypes = String.join("," ,jobTypes);
                Placeholder placeholder = new Placeholder("JOB_TYPES",joinJobTypes);
                return Optional.of(errorFactory.buildError("ERROR_CODE_For_Tariff_4",List.of(placeholder)));
            }
        }else {
            return Optional.of(errorFactory.buildError("ERROR_CODE_For_Tariff_5"));
        }
        return Optional.empty();
    }

    public Optional<ValidationError> suchTariffAlwaysIsInDatabase(String jobType){
        return (tariffRepository.findByJobType(jobType).isPresent())
                ? Optional.of(errorFactory.buildError("ERROR_CODE_For_Tariff_6"))
                : Optional.empty();
    }

    private boolean isNullOrBlankOrEmpty(String parameter) {
        return parameter == null || parameter.isBlank() || parameter.isEmpty();
    }
}
