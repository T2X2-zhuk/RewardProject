package RewardCalculation.service.servises;

import RewardCalculation.JPA.domain.Employee;
import RewardCalculation.responses.RewardCalculationResponse;
import RewardCalculation.restClientRewardPayment.RewardPaymentResponse;
import RewardCalculation.service.calculate.CalculatePayment;
import RewardCalculation.service.calculate.SendPaymentsToMicroservice;
import RewardCalculation.service.calculate.SuccessfulPayment;
import RewardCalculation.service.interfaces.RewardCalculationService;
import RewardCalculation.util.ValidationError;
import RewardCalculation.validations.validators.ListEmployeeValidator;
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

    public RewardCalculationResponse execute(List<Employee> employees) {
        RewardCalculationResponse coreResponse = new RewardCalculationResponse();
        List<ValidationError> validationErrors = validator.validate(employees);
        if (!validationErrors.isEmpty()) {
            coreResponse.setErrors(validationErrors);
            return coreResponse;
        }
        return appealAndResponseToRewardPayment(employees, coreResponse);
    }

    private RewardCalculationResponse appealAndResponseToRewardPayment(List<Employee> employees, RewardCalculationResponse coreResponse) {
        RewardPaymentResponse paymentResponse = sendingToMicroservice.send(calculatePayment.calculate(employees));

        if (paymentResponse.hasErrors()){
            coreResponse.setErrors(paymentResponse.getErrors());
            return coreResponse;
        }
        else {
            return successfulPayment.execute(coreResponse, paymentResponse);
        }
    }
}
