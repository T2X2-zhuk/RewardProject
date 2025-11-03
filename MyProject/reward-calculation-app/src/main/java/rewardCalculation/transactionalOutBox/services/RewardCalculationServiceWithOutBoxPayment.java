package rewardCalculation.transactionalOutBox.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import rewardCalculation.jpa.domain.Reward;
import rewardCalculation.restClientRewardPayment.CalculatePayment;
import rewardCalculation.dto.PaymentDTO;
import rewardCalculation.rest.commonServiceInterfices.RewardCalculationService;
import rewardCalculation.validations.validators.reward.RewardCalculationValidator;
import rewardCalculation.restClientRewardPayment.RewardPaymentResponse;
import rewardCalculation.util.forErrors.ValidationError;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnExpression("${app.transactionalOutBox:true} == true")
class RewardCalculationServiceWithOutBoxPayment implements RewardCalculationService {
    
    private final RewardCalculationValidator validator;
    private final CalculatePayment calculatePayment;
    private final RewardOutboxService rewardOutboxService;

    public RewardPaymentResponse execute(List<Reward> rewardList) {

        log.info("{} is start!", this.getClass().getSimpleName());

        RewardPaymentResponse coreResponse = new RewardPaymentResponse();

        List<ValidationError> validationErrors = validator.validate(rewardList);

        if (!validationErrors.isEmpty()) {

            log.warn("Request validation failed : {}",validationErrors);

            coreResponse.setErrors(validationErrors);

            return coreResponse;
        }

        List<PaymentDTO> paymentDTOS = calculatePayment.calculate(rewardList);

        // платежи рассчитаны и записаны в Outbox
        rewardOutboxService.schedulePayments(paymentDTOS,rewardList);

        log.info("Payments scheduled to Outbox and Rewards set to PROCESSING");
        coreResponse.setSuccessfulSaving(true);

        log.info("{} is execute!", this.getClass().getSimpleName());
        return coreResponse;
    }
}
