package rewardCalculation.servises.reward;

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
class RewardCalculationServiceImpl implements RewardCalculationService {
    
    @Autowired private ListEmployeeValidator validator;
    @Autowired private SuccessfulPayment successfulPayment;
    @Autowired private SendPaymentsToMicroservice sendingToMicroservice;
    @Autowired private CalculatePayment calculatePayment;

    public RewardPaymentResponse execute(List<Employee> employees) {
        RewardPaymentResponse coreResponse = new RewardPaymentResponse();
        List<ValidationError> validationErrors = validator.validate(employees);
        if (!validationErrors.isEmpty()) {
            coreResponse.setErrors(validationErrors);
            return coreResponse;
        }
        return appealAndResponseToRewardPayment(employees);
    }

    private RewardPaymentResponse appealAndResponseToRewardPayment(List<Employee> employees) {
        RewardPaymentResponse paymentResponse = sendingToMicroservice.send(calculatePayment.calculate(employees));

        if (paymentResponse.hasErrors()){
            return paymentResponse;
        }
        else {
            return successfulPayment.execute(paymentResponse);
        }
    }
}
