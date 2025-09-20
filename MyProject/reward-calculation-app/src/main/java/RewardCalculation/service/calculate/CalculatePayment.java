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
import java.util.Optional;

@Component
public class CalculatePayment {

    @Autowired private TariffRepository tariffRepository;
    @Autowired private RewardRepository rewardRepository;

    public List<PaymentDTO> calculate(List<Employee> employees){
        List<Tariff> tariffs = tariffRepository.findAll();
        return employees.stream()
                .flatMap(employee -> rewardRepository.findByEmployeeId(employee.getId()).stream()
                        .map(reward -> createPaymentDto(employee, reward, tariffs)))
                .toList();
    }
    private PaymentDTO createPaymentDto(Employee employee , Reward reward,List<Tariff> tariffs){
        Optional<Tariff> tariff = tariffs.stream()
                .filter(t -> reward.getJobType().equals(t.getJobType()))
                .findFirst();
       return new PaymentDTO(employee.getId(), getAmount(employee.getBonusCoefficient(),tariff.get().getAmount()));
    }
    private double getAmount(Double bonusCoefficient , Double amount){
        return (1 + bonusCoefficient) * amount;
    }
}
