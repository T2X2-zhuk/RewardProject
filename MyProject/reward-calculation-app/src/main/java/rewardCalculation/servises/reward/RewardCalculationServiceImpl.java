package rewardCalculation.servises.reward;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.validations.validators.employee.ListEmployeeValidator;
import rewardCalculation.JPA.domain.Employee;
import rewardCalculation.calculate.CalculatePayment;
import rewardCalculation.calculate.SuccessfulPayment;
import rewardCalculation.restClientRewardPayment.RewardPaymentResponse;
import rewardCalculation.calculate.SendPaymentsToMicroservice;
import rewardCalculation.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
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
@ToString
@Slf4j
class RewardCalculationServiceImpl implements RewardCalculationService {
    
    private final ListEmployeeValidator validator;
    private final SuccessfulPayment successfulPayment;
    private final SendPaymentsToMicroservice sendingToMicroservice;
    private final CalculatePayment calculatePayment;

    public RewardPaymentResponse execute(List<Employee> employees) {
        log.info("{} is start!", this);
        RewardPaymentResponse coreResponse = new RewardPaymentResponse();
        List<ValidationError> validationErrors = validator.validate(employees);
        if (!validationErrors.isEmpty()) {
            log.warn("Request validation failed : {}",validationErrors);
            coreResponse.setErrors(validationErrors);
            return coreResponse;
        }
        RewardPaymentResponse paymentResponse = appealAndResponseToRewardPayment(employees);
        log.info("{} is execute!", this);
        return paymentResponse;
    }

    private RewardPaymentResponse appealAndResponseToRewardPayment(List<Employee> employees) {
        RewardPaymentResponse paymentResponse = sendingToMicroservice.send(calculatePayment.calculate(employees));
        log.debug("Payment sent successfully!");
        if (paymentResponse.hasErrors()){
            log.warn("Payment response has errors: {}",paymentResponse.getErrors());
            return paymentResponse;
        }
        else {
            log.debug("Payments is - {}", paymentResponse.getStatus());
            log.info("Execute!");
            return successfulPayment.execute(paymentResponse);
        }
    }
}
