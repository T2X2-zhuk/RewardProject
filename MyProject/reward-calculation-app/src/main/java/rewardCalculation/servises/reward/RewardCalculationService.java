package rewardCalculation.servises.reward;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.JPA.domain.Reward;
import rewardCalculation.util.forServices.AppealAndResponseToRewardPayment;
import rewardCalculation.validations.validators.reward.RewardCalculationValidator;
import rewardCalculation.JPA.domain.Employee;
import rewardCalculation.restClientRewardPayment.RewardPaymentResponse;
import rewardCalculation.util.forError.ValidationError;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
public class RewardCalculationService {
    
    private final RewardCalculationValidator validator;
    private final AppealAndResponseToRewardPayment appealAndResponseToRewardPayment;


    public RewardPaymentResponse execute(List<Employee> employees,List<Reward> rewardList) {
        log.info("{} is start!", this.getClass().getSimpleName());
        RewardPaymentResponse coreResponse = new RewardPaymentResponse();
        List<ValidationError> validationErrors = validator.validate(employees,rewardList);
        if (!validationErrors.isEmpty()) {
            log.warn("Request validation failed : {}",validationErrors);
            coreResponse.setErrors(validationErrors);
            return coreResponse;
        }
        RewardPaymentResponse paymentResponse = appealAndResponseToRewardPayment.successful(employees,rewardList);
        log.info("{} is execute!", this.getClass().getSimpleName());
        return paymentResponse;
    }
}
