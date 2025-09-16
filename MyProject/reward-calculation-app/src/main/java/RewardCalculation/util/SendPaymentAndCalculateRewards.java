package RewardCalculation.util;

import RewardCalculation.JPA.domain.Employee;
import RewardCalculation.JPA.domain.Reward;
import RewardCalculation.JPA.domain.Tariff;
import RewardCalculation.JPA.repositories.RewardRepository;
import RewardCalculation.JPA.repositories.TariffRepository;
import RewardCalculation.restClientRewardPayment.RewardPaymentClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SendPaymentAndCalculateRewards {

    @Autowired
    private RewardPaymentClient rewardPaymentClient;
    @Autowired private TariffRepository tariffRepository;
    @Autowired private RewardRepository rewardRepository;

    public void calculate(List<Employee> employees){
        for (Employee employee : employees) {
            List<Reward> rewards = rewardRepository.findByEmployeeId(employee.getId());
            for (Reward reward : rewards) {
                sendingPayment(employee,reward);
            }
        }
    }
    private void sendingPayment(Employee employee , Reward reward){
       Tariff tariff = tariffRepository.findByJobType(reward.getJobType()).get();
       rewardPaymentClient.payReward(employee.getId(), getAmount(employee.getBonusCoefficient(),tariff.getAmount()));
       System.out.println("Отправлен платеж");
       reward.setStatus("PAID");
       rewardRepository.save(reward);
    }

    private double getAmount(Double bonusCoefficient , Double amount){
        return (1 + bonusCoefficient) * amount;
    }
}
