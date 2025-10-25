package rewardCalculation.servises.reward;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import rewardCalculation.JPA.domain.Reward;
import rewardCalculation.util.forServices.CalculatePayment;
import rewardCalculation.rest.commonServiceInterfices.RewardCalculationService;
import rewardCalculation.restClientRewardPayment.RewardPaymentClient;
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
        havingValue = "false"
)
class RewardCalculationServiceWithoutOutBoxing implements RewardCalculationService {

    private final RewardCalculationValidator validator;
    private final CalculatePayment calculatePayment;
    private final RewardPaymentClient client;
    private final UpdatingRewardsDependingOnTheResultReceivedFromAnExternalService updatingRewardsDependingOnTheResultReceivedFromAnExternalService;

    @Override
    public RewardPaymentResponse execute(List<Reward> rewardList) {
        log.info("{} is start!", this.getClass().getSimpleName());
        RewardPaymentResponse coreResponse = new RewardPaymentResponse();
        List<ValidationError> validationErrors = validator.validate(rewardList);
        if (!validationErrors.isEmpty()) {
            log.warn("Request validation failed : {}",validationErrors);
            coreResponse.setErrors(validationErrors);
            return coreResponse;
        }
        coreResponse = client.payReward(calculatePayment.calculate(rewardList));
        log.info("{} is execute!", this.getClass().getSimpleName());
        return updatingRewardsDependingOnTheResultReceivedFromAnExternalService.result(coreResponse,rewardList);
    }
}
