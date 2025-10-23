package rewardCalculation.validations.MethodsValidatorClasses;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.JPA.domain.Tariff;
import rewardCalculation.JPA.repositories.TariffRepository;
import rewardCalculation.cache.get.GetTariffUsingCache;
import rewardCalculation.util.forError.Placeholder;
import rewardCalculation.util.forError.ValidationError;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidatorClassWithMethodsForTariff {

     private final TariffRepository tariffRepository;
     private final ValidationErrorFactory errorFactory;
     private final GetTariffUsingCache getTariffUsingCache;

    public Optional<ValidationError> isSuchTariffIsNull(Long id){
        if (tariffRepository.findById(id).isEmpty()){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_FOR_TARIFF_2"));
            log.debug("Error : {}",error);
            return error;
        }
       return Optional.empty();
    }

    public Optional<ValidationError> amountMustNotBeEmpty(BigDecimal amount){
        if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_FOR_TARIFF_3"));
            log.debug("Error : {}",error);
            return error;
        }
        return Optional.empty();
    }
    public Optional<ValidationError> suchTariffWithSuchJobTypeIsNotInDatabase(String jobType){
        Map<String, Tariff> tariffsMap = getTariffUsingCache.getTariffsByJobType();
        if (!tariffsMap.containsKey(jobType.toUpperCase())) {
            Optional<ValidationError> error = tariffsMap.isEmpty()
                    ? Optional.of(errorFactory.buildError("ERROR_CODE_FOR_TARIFF_5"))
                    : Optional.of(errorFactory.buildError("ERROR_CODE_FOR_TARIFF_4",
                    List.of(new Placeholder("TARIFFS",
                            tariffsMap.entrySet().stream()
                                    .map(entry -> entry.getKey() + "=" + entry.getValue().getAmount())
                                    .collect(Collectors.joining(","))
                    ))
            ));
            log.debug("Error : {}", error);
            return error;
        }
        return Optional.empty();
    }

    public Optional<ValidationError> suchTariffAlwaysIsInDatabase(String jobType){
        // Берём Map из кеша
        Map<String, Tariff> tariffsMap = getTariffUsingCache.getTariffsByJobType();
        if (tariffsMap.containsKey(jobType.toUpperCase())){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_FOR_TARIFF_6"));
            log.debug("Error : {}",error);
            return error;
        }
        return Optional.empty();
    }

    private boolean isNullOrBlankOrEmpty(String parameter) {
        return parameter == null || parameter.isBlank();
    }
}
