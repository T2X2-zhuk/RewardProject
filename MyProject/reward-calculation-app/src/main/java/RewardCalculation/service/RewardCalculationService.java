package RewardCalculation.service;

import RewardCalculation.JPA.repositories.TariffRepository;
import RewardCalculation.JPA.domain.Employee;
import RewardCalculation.JPA.domain.Reward;
import RewardCalculation.JPA.domain.Tariff;
import RewardCalculation.JPA.repositories.RewardRepository;
import RewardCalculation.restClientRewardPayment.RewardPaymentClient;
import RewardCalculation.util.SendPaymentAndCalculateRewards;
import RewardCalculation.validations.MethodsValidatorClasses.ValidatorClassWithMethodsForJobTypes;
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
public class RewardCalculationService {

    @Autowired private ValidatorClassWithMethodsForJobTypes validator;
    @Autowired private SendPaymentAndCalculateRewards calculateRewards;

    public void execute(List<Employee> employees) {
        if (validator.isSuchJobTypesInDatabase(employees).isEmpty()){
           calculateRewards.calculate(employees);
        }
    }
}
