package rewardPayment.validations.validators;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import rewardPayment.dto.PaymentDTO;
import rewardPayment.requests.CommonRequestForPaymentParameters;
import rewardPayment.util.forErrors.ValidationError;
import rewardPayment.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForPayment;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetPaymentValidator {

    private final ValidatorClassWithMethodsForPayment validatorClassWithMethodsForPayment;

    public Mono<List<ValidationError>> validate(CommonRequestForPaymentParameters request) {
        log.info("{} start!", this.getClass().getSimpleName());

        return validatorClassWithMethodsForPayment.isSuchPaymentInDatabase(request.getPaymentDTO().getEmployeeId())
                .map(optionalError -> {
                    List<ValidationError> errors = new ArrayList<>();
                    optionalError.ifPresent(errors::add);
                    log.info("{} execute! Errors: {}", this.getClass().getSimpleName(), errors);
                    return errors;
                });
    }
}
