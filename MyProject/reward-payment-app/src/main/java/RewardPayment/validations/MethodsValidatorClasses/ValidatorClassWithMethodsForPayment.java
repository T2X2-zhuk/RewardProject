package RewardPayment.validations.MethodsValidatorClasses;
import RewardPayment.configCache.GetAllPaymentsUsingCache;
import RewardPayment.util.ValidationError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidatorClassWithMethodsForPayment {

    private final ValidationErrorFactory errorFactory;
    private final GetAllPaymentsUsingCache getAllPaymentsUsingCache;

    public Optional<ValidationError> employeeIdNotBeEmpty(Long employeeId){
        if (employeeId == null){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_FOR_PAYMENT_1"));
            log.debug("Error : {}",error);
            return error;
        }
       return Optional.empty();
    }

    public Optional<ValidationError> amountMustNotBeEmpty(BigDecimal amount){
        if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_FOR_PAYMENT_2"));
            log.debug("Error : {}",error);
            return error;
        }
        return Optional.empty();
    }

    public Optional<ValidationError> isSuchPaymentInDatabase(Long employeeId,BigDecimal amount){
        boolean exists = getAllPaymentsUsingCache.getAllPaymentsWithCache().stream()
                .anyMatch(p -> p.getEmployeeId().equals(employeeId)
                        && p.getAmount().compareTo(amount) == 0);
        if (!exists){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_FOR_PAYMENT_3"));
            log.debug("Error : {}",error);
            return error;
        }
        return Optional.empty();
    }
}
