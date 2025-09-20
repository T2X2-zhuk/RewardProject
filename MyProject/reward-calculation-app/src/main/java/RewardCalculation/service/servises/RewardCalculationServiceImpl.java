package RewardCalculation.service.servises;

import RewardCalculation.JPA.domain.Employee;
import RewardCalculation.JPA.domain.Reward;
import RewardCalculation.JPA.repositories.RewardRepository;
import RewardCalculation.dto.PaymentDTO;
import RewardCalculation.responses.RewardCalculationResponse;
import RewardCalculation.restClientRewardPayment.RewardPaymentResponse;
import RewardCalculation.service.calculate.CalculatePayment;
import RewardCalculation.service.calculate.SendPaymentsToMicroservice;
import RewardCalculation.service.interfaces.RewardCalculationService;
import RewardCalculation.util.ValidationError;
import RewardCalculation.validations.validators.ListEmployeeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    @Autowired private RewardRepository rewardRepository;
    @Autowired private SendPaymentsToMicroservice sendingToMicroservice;
    @Autowired private CalculatePayment calculatePayment;

    public RewardCalculationResponse execute(List<Employee> employees) {
        RewardCalculationResponse coreResponse = new RewardCalculationResponse();
        List<ValidationError> errors = validator.validate(employees);
        if (!errors.isEmpty()){
            coreResponse.setErrors(errors);
            return coreResponse;
        }
        List<PaymentDTO> paymentDTOSList = calculatePayment.calculate(employees);
        RewardPaymentResponse paymentResponse = sendingToMicroservice.send(paymentDTOSList);
        if (paymentResponse.hasErrors()){
            coreResponse.setErrors(paymentResponse.getErrors());
            return coreResponse;
        }else {
            successfulResponse(coreResponse,paymentResponse,paymentDTOSList);
        }
        return coreResponse;
    }

    private void successfulResponse(RewardCalculationResponse coreResponse,RewardPaymentResponse paymentResponse,List<PaymentDTO> paymentDTOSList) {
        coreResponse.setMessage("Ответ успешен - " + paymentResponse.getStatus());
        List<Reward> rewards = new ArrayList<>();
        for (PaymentDTO paymentDTO : paymentDTOSList){
            rewards.addAll(rewardRepository.findByEmployeeId(paymentDTO.getEmployeeId()));
        }
        rewardSetStatusOnPaid(rewards, paymentResponse.getStatus());
    }
    private void rewardSetStatusOnPaid(List<Reward> rewardsAfterPayment , String status){
        rewardsAfterPayment.forEach(reward ->  rewardRepository.rewardSetStatus(status ,reward.getEmployeeId()));
    }
}
