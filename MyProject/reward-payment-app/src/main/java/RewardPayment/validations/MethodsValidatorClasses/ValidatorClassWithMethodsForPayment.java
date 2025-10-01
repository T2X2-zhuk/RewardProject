package RewardPayment.validations.MethodsValidatorClasses;
import RewardPayment.JPA.repositories.PaymentRepository;
import RewardPayment.util.ValidationError;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidatorClassWithMethodsForPayment {

    private final PaymentRepository repository;
    private final ValidationErrorFactory errorFactory;

    public Optional<ValidationError> employeeIdNotBeEmpty(Long employeeId){
        if (employeeId == null){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_FOR_PAYMENT_1"));
            log.debug("Error : {}",error);
            return error;
        }
       return Optional.empty();
    }

    public Optional<ValidationError> amountMustNotBeEmpty(Double amount){
        if (amount == 0.0){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_FOR_PAYMENT_2"));
            log.debug("Error : {}",error);
            return error;
        }
        return Optional.empty();
    }

    public Optional<ValidationError> isSuchPaymentInDatabase(Long employeeId,Double amount){
        if (repository.findByEmployeeIdAndAmount(employeeId,amount).isEmpty()){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_FOR_PAYMENT_3"));
            log.debug("Error : {}",error);
            return error;
        }
        return Optional.empty();
    }
}
