package rewardCalculation.servises.reward;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.JPA.domain.EnumObject.RewardStatus;
import rewardCalculation.JPA.domain.Reward;
import rewardCalculation.JPA.repositories.RewardRepository;
import rewardCalculation.dto.PaymentDTO;
import rewardCalculation.validations.validators.reward.RewardCalculationValidator;
import rewardCalculation.JPA.domain.Employee;
import rewardCalculation.calculate.CalculatePayment;
import rewardCalculation.restClientRewardPayment.RewardPaymentResponse;
import rewardCalculation.calculate.SendPaymentsToRewardPaymentApplication;
import rewardCalculation.util.ValidationError;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис тарификации вознаграждений сотрудникам за дополнительную работу.
 * Каждый сотрудник может выполнять что-либо помимо основной работы - проводить лекции, выступать на конференциях и т.д.
 * Такие действия оплачиваются согласно тарифам, с учетом заслуг сотрудника (личного бонусного коэффициента).
 * Оплата проходит через внешний сервис, вызываемый по REST.
 */

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
class RewardCalculationServiceImpl implements RewardCalculationService {
    
    private final RewardCalculationValidator validator;
    private final RewardRepository rewardRepository;
    private final SendPaymentsToRewardPaymentApplication sendingToMicroservice;
    private final CalculatePayment calculatePayment;

    public RewardPaymentResponse execute(List<Employee> employees,List<Reward> rewardList) {
        log.info("{} is start!", this.getClass().getSimpleName());
        RewardPaymentResponse coreResponse = new RewardPaymentResponse();
        List<ValidationError> validationErrors = validator.validate(employees,rewardList);
        if (!validationErrors.isEmpty()) {
            log.warn("Request validation failed : {}",validationErrors);
            coreResponse.setErrors(validationErrors);
            return coreResponse;
        }
        RewardPaymentResponse paymentResponse = appealAndResponseToRewardPayment(employees,rewardList);
        log.info("{} is execute!", this.getClass().getSimpleName());
        return paymentResponse;
    }

    private RewardPaymentResponse appealAndResponseToRewardPayment(List<Employee> employees,List<Reward> rewardList) {
        List<PaymentDTO> paymentDTOS = calculatePayment.calculate(employees,rewardList);
        RewardPaymentResponse paymentResponse = sendingToMicroservice.send(paymentDTOS);
        log.debug("Payment sent successfully!");
        if (paymentResponse.hasErrors() || !paymentResponse.isSuccessfulSaving()){
            log.warn("Payment response has errors: {}",paymentResponse.getErrors());
        }
        else {
            List<Long> ids = rewardList.stream()
                    .map(Reward::getId)
                    .toList();
            rewardRepository.rewardSetStatusForList(RewardStatus.PAID, ids);
            log.debug("Rewards is -{} ",RewardStatus.PAID);
            log.info("Execute!");
        }
        return paymentResponse;
    }
}
