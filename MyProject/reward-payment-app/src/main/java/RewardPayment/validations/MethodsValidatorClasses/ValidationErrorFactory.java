package RewardPayment.validations.MethodsValidatorClasses;

import RewardPayment.util.ErrorCodeUtil;
import RewardPayment.util.Placeholder;
import RewardPayment.util.ValidationError;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ValidationErrorFactory {

    private final ErrorCodeUtil errorCodeUtil;

    public ValidationError buildError(String errorCode) {
        String errorDescription = errorCodeUtil.getErrorDescription(errorCode);
        return new ValidationError(errorCode, errorDescription);
    }

    public ValidationError buildError(String errorCode, List<Placeholder> placeholders) {
        String errorDescription = errorCodeUtil.getErrorDescription(errorCode, placeholders);
        return new ValidationError(errorCode, errorDescription);
    }

}
