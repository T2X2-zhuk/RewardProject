package rewardPayment.validations.MethodsValidatorClasses;
import reactor.core.publisher.Mono;
import rewardPayment.configCache.GetAllPaymentsUsingCache;
import rewardPayment.util.forErrors.Placeholder;
import rewardPayment.util.forErrors.ValidationError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidatorClassWithMethodsForPayment {

    private final ValidationErrorFactory errorFactory;
    private final GetAllPaymentsUsingCache getAllPaymentsUsingCache;

    public Mono<Optional<ValidationError>> employeeIdNotBeEmptyReactive(Long employeeId) {
        if (employeeId == null) {
            return Mono.just(Optional.of(errorFactory.buildError("ERROR_CODE_FOR_PAYMENT_1")));
        }
        return Mono.just(Optional.empty());
    }

    public Mono<Optional<ValidationError>> amountMustNotBeEmptyReactive(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
            return Mono.just(Optional.of(errorFactory.buildError("ERROR_CODE_FOR_PAYMENT_2")));
        }
        return Mono.just(Optional.empty());
    }

    public Mono<Optional<ValidationError>> isSuchPaymentInDatabase(Long employeeId){

        return getAllPaymentsUsingCache.getAllPaymentsWithCache()
                .filter(p -> p.getEmployeeId().equals(employeeId))
                .hasElements()
                .map(exists -> {
                    if (!exists) {
                        return Optional.of(errorFactory.buildError(
                                "ERROR_CODE_FOR_PAYMENT_3",
                                List.of(new Placeholder("id", String.valueOf(employeeId)))
                        ));
                    }
                    return Optional.empty();
                });
    }
}
