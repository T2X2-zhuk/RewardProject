package rewardCalculation.calculate;

import rewardCalculation.JPA.domain.Employee;
import rewardCalculation.JPA.domain.Reward;
import rewardCalculation.JPA.domain.Tariff;
import rewardCalculation.JPA.repositories.RewardRepository;
import rewardCalculation.JPA.repositories.TariffRepository;
import rewardCalculation.dto.PaymentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        return PaymentDTO.builder()
                .employeeId(employee.getId())
                .amount(getAmount(employee.getBonusCoefficient(),tariff.get().getAmount())).build();
    }
    private double getAmount(Double bonusCoefficient , Double amount){
        return (1 + bonusCoefficient) * amount;
    }
}
