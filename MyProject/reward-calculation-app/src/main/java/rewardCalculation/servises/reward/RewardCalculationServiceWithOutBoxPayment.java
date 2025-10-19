package rewardCalculation.servises.reward;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import rewardCalculation.JPA.domain.Reward;
import rewardCalculation.calculate.CalculatePayment;
import rewardCalculation.dto.PaymentDTO;
import rewardCalculation.util.forServices.RewardOutboxService;
import rewardCalculation.validations.validators.reward.RewardCalculationValidator;
import rewardCalculation.JPA.domain.Employee;
import rewardCalculation.restClientRewardPayment.RewardPaymentResponse;
import rewardCalculation.util.forError.ValidationError;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(
        prefix = "app",
        name = "transactionalOutBox",
        havingValue = "true"
)
class RewardCalculationServiceWithOutBoxPayment implements RewardCalculationService {
    
    private final RewardCalculationValidator validator;
    private final CalculatePayment calculatePayment;
    private final RewardOutboxService rewardOutboxService;

    public RewardPaymentResponse execute(List<Employee> employees,List<Reward> rewardList) {
        log.info("{} is start!", this.getClass().getSimpleName());
        RewardPaymentResponse coreResponse = new RewardPaymentResponse();
        List<ValidationError> validationErrors = validator.validate(employees,rewardList);
        if (!validationErrors.isEmpty()) {
            log.warn("Request validation failed : {}",validationErrors);
            coreResponse.setErrors(validationErrors);
            return coreResponse;
        }
        List<PaymentDTO> paymentDTOS = calculatePayment.calculate(employees, rewardList);
        // платежи рассчитаны и записаны в Outbox
        rewardOutboxService.schedulePayments(paymentDTOS,rewardList);
        log.info("Payments scheduled to Outbox and Rewards set to PROCESSING");
        coreResponse.setSuccessfulSaving(true);
        log.info("{} is execute!", this.getClass().getSimpleName());
        return coreResponse;
    }
}
