package RewardCalculation.service.calculate;

import RewardCalculation.JPA.domain.Employee;
import RewardCalculation.JPA.domain.Reward;
import RewardCalculation.JPA.domain.Tariff;
import RewardCalculation.JPA.repositories.RewardRepository;
import RewardCalculation.JPA.repositories.TariffRepository;
import RewardCalculation.dto.PaymentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CalculatePayment {

    @Autowired private TariffRepository tariffRepository;
    @Autowired private RewardRepository rewardRepository;

    public List<PaymentDTO> calculate(List<Employee> employees){
        List<PaymentDTO> paymentDTOS = new ArrayList<>();
        for (Employee employee : employees) {
            for (Reward reward : rewardRepository.findByEmployeeId(employee.getId())) {
                paymentDTOS.add(createPaymentDto(employee,reward));
            }
        }
        return paymentDTOS;
    }
    private PaymentDTO createPaymentDto(Employee employee , Reward reward){
       Tariff tariff = tariffRepository.findByJobType(reward.getJobType()).get();
       return new PaymentDTO(employee.getId(), getAmount(employee.getBonusCoefficient(),tariff.getAmount()));
    }
    private double getAmount(Double bonusCoefficient , Double amount){
        return (1 + bonusCoefficient) * amount;
    }
}
