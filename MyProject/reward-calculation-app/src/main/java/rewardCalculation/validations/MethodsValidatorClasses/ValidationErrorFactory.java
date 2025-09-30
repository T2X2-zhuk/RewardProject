package rewardCalculation.validations.MethodsValidatorClasses;

import lombok.RequiredArgsConstructor;
import rewardCalculation.util.ErrorCodeUtil;
import rewardCalculation.util.Placeholder;
import rewardCalculation.util.ValidationError;
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
