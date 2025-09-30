package RewardPayment.validations.MethodsValidatorClasses;
import RewardPayment.JPA.repositories.PaymentRepository;
import RewardPayment.util.ValidationError;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ValidatorClassWithMethodsForPayment {

    private final PaymentRepository repository;
    private final ValidationErrorFactory errorFactory;

    public Optional<ValidationError> employeeIdNotBeEmpty(Long employeeId){
      return (employeeId == null)
      ? Optional.of(errorFactory.buildError("ERROR_CODE_FOR_PAYMENT_1"))
      : Optional.empty();
    }

    public Optional<ValidationError> amountMustNotBeEmpty(Double amount){
        return (amount == 0.0)
                ? Optional.of(errorFactory.buildError("ERROR_CODE_FOR_PAYMENT_2"))
                : Optional.empty();
    }

    public Optional<ValidationError> isSuchPaymentInDatabase(Long employeeId,Double amount){
        return (repository.findByEmployeeIdAndAmount(employeeId,amount).isEmpty())
                ? Optional.of(errorFactory.buildError("ERROR_CODE_FOR_PAYMENT_3"))
                : Optional.empty();
    }
}
