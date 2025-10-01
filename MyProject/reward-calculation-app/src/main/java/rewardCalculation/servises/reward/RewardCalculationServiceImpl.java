package rewardCalculation.servises.reward;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.JPA.domain.EnumObject.RewardStatus;
import rewardCalculation.dto.PaymentDTO;
import rewardCalculation.validations.validators.employee.ListEmployeeValidator;
import rewardCalculation.JPA.domain.Employee;
import rewardCalculation.calculate.CalculatePayment;
import rewardCalculation.calculate.SuccessfulPayment;
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
    
    private final ListEmployeeValidator validator;
    private final SuccessfulPayment successfulPayment;
    private final SendPaymentsToRewardPaymentApplication sendingToMicroservice;
    private final CalculatePayment calculatePayment;

    public RewardPaymentResponse execute(List<Employee> employees) {
        log.info("{} is start!", this.getClass().getSimpleName());
        RewardPaymentResponse coreResponse = new RewardPaymentResponse();
        List<ValidationError> validationErrors = validator.validate(employees);
        if (!validationErrors.isEmpty()) {
            log.warn("Request validation failed : {}",validationErrors);
            coreResponse.setErrors(validationErrors);
            return coreResponse;
        }
        RewardPaymentResponse paymentResponse = appealAndResponseToRewardPayment(employees);
        log.info("{} is execute!", this.getClass().getSimpleName());
        return paymentResponse;
    }

    private RewardPaymentResponse appealAndResponseToRewardPayment(List<Employee> employees) {
        List<PaymentDTO> paymentDTOS = calculatePayment.calculate(employees);
        RewardPaymentResponse paymentResponse = sendingToMicroservice.send(paymentDTOS);
        log.debug("Payment sent successfully!");
        if (paymentResponse.hasErrors() || !paymentResponse.isSuccessfulSaving()){
            log.warn("Payment response has errors: {}",paymentResponse.getErrors());
        }
        else {
            log.debug("Payments is - {}", RewardStatus.PAID);
            log.info("Execute!");
            successfulPayment.execute(paymentDTOS);
        }
        return paymentResponse;
    }
}
