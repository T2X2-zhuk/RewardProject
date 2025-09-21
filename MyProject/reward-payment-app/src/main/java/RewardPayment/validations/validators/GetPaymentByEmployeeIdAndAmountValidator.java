package RewardPayment.validations.validators;

import RewardPayment.util.ValidationError;
import RewardPayment.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GetPaymentByEmployeeIdAndAmountValidator {

    @Autowired private ValidatorClassWithMethodsForPayment validatorClassWithMethodsForPayment;

    public List<ValidationError> validate(Long employeeId, Double amount){
        List<ValidationError> errors = new ArrayList<>();
        validatorClassWithMethodsForPayment.employeeIdNotBeEmpty(employeeId).ifPresent(errors::add);
        validatorClassWithMethodsForPayment.amountMustNotBeEmpty(amount).ifPresent(errors::add);
        if (errors.isEmpty()){
            validatorClassWithMethodsForPayment.isSuchPaymentInDatabase(employeeId,amount).ifPresent(errors::add);
        }
        return errors;
    }
}
