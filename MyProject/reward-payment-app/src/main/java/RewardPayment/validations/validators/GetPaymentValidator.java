package RewardPayment.validations.validators;

import RewardPayment.requests.CommonRequestForPaymentParameters;
import RewardPayment.util.forErrors.ValidationError;
import RewardPayment.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForPayment;
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
        log.info("{} execute!",this.getClass().getSimpleName());
        return errors;
    }
}
