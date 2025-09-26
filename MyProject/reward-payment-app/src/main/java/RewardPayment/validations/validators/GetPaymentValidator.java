package RewardPayment.validations.validators;

import RewardPayment.requests.CommonRequestForPaymentParameters;
import RewardPayment.util.ValidationError;
import RewardPayment.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GetPaymentValidator {

    @Autowired private ValidatorClassWithMethodsForPayment validatorClassWithMethodsForPayment;

    public List<ValidationError> validate(CommonRequestForPaymentParameters request){
        List<ValidationError> errors = new ArrayList<>();
        validatorClassWithMethodsForPayment.employeeIdNotBeEmpty(request.getPaymentDTO().getEmployeeId()).ifPresent(errors::add);
        validatorClassWithMethodsForPayment.amountMustNotBeEmpty(request.getPaymentDTO().getAmount()).ifPresent(errors::add);
        if (errors.isEmpty()){
            validatorClassWithMethodsForPayment.isSuchPaymentInDatabase(request.getPaymentDTO().getEmployeeId(),request.getPaymentDTO().getAmount()).ifPresent(errors::add);
        }
        return errors;
    }
}
