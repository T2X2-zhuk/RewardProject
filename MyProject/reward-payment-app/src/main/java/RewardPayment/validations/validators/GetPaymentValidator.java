package RewardPayment.validations.validators;

import RewardPayment.requests.CommonRequestForPaymentParameters;
import RewardPayment.util.ValidationError;
import RewardPayment.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForPayment;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetPaymentValidator {

    private final ValidatorClassWithMethodsForPayment validatorClassWithMethodsForPayment;

    public List<ValidationError> validate(CommonRequestForPaymentParameters request){
        List<ValidationError> errors = new ArrayList<>();
        validatorClassWithMethodsForPayment.isSuchPaymentInDatabase(request.getPaymentDTO().getEmployeeId(),request.getPaymentDTO().getAmount()).ifPresent(errors::add);
        return errors;
    }
}
