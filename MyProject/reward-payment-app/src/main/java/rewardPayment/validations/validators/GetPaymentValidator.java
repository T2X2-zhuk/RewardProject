package rewardPayment.validations.validators;

import rewardPayment.requests.CommonRequestForPaymentParameters;
import rewardPayment.util.forErrors.ValidationError;
import rewardPayment.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForPayment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetPaymentValidator {

    private final ValidatorClassWithMethodsForPayment validatorClassWithMethodsForPayment;

    public List<ValidationError> validate(CommonRequestForPaymentParameters request){
        log.info("{} start!",this.getClass().getSimpleName());
        List<ValidationError> errors = new ArrayList<>();
        validatorClassWithMethodsForPayment.isSuchPaymentInDatabase(request.getPaymentDTO().getEmployeeId()).ifPresent(errors::add);
        log.info("{} execute! Errors : {}",this.getClass().getSimpleName(),errors);
        return errors;
    }
}
