package rewardCalculation.servises.reward;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import rewardCalculation.JPA.domain.Employee;
import rewardCalculation.JPA.domain.Reward;
import rewardCalculation.calculate.CalculatePayment;
import rewardCalculation.calculate.SendPaymentsToRewardPaymentApplication;
import rewardCalculation.restClientRewardPayment.RewardPaymentResponse;
import rewardCalculation.util.forError.ValidationError;
import rewardCalculation.util.forServices.UpdatingRewardsDependingOnTheResultReceivedFromAnExternalService;
import rewardCalculation.validations.validators.reward.RewardCalculationValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(
        prefix = "app",
        name = "transactionalOutBox",
        havingValue = "false",
        matchIfMissing = true
)
class RewardCalculationServiceWithoutOutBoxing implements RewardCalculationService{

    private final RewardCalculationValidator validator;
    private final CalculatePayment calculatePayment;
    private final SendPaymentsToRewardPaymentApplication sendPaymentsToRewardPaymentApplication;
    private final UpdatingRewardsDependingOnTheResultReceivedFromAnExternalService updatingRewardsDependingOnTheResultReceivedFromAnExternalService;

    @Override
    public RewardPaymentResponse execute(List<Employee> employees, List<Reward> rewardList) {
        log.info("{} is start!", this.getClass().getSimpleName());
        RewardPaymentResponse coreResponse = new RewardPaymentResponse();
        List<ValidationError> validationErrors = validator.validate(employees,rewardList);
        if (!validationErrors.isEmpty()) {
            log.warn("Request validation failed : {}",validationErrors);
            coreResponse.setErrors(validationErrors);
            return coreResponse;
        }
        coreResponse = sendPaymentsToRewardPaymentApplication.send(calculatePayment.calculate(employees, rewardList));
        log.info("{} is execute!", this.getClass().getSimpleName());
        return updatingRewardsDependingOnTheResultReceivedFromAnExternalService.result(coreResponse,rewardList);
    }
}
