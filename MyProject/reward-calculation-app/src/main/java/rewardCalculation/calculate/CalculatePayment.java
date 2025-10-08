package rewardCalculation.calculate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.JPA.domain.Employee;
import rewardCalculation.JPA.domain.Reward;
import rewardCalculation.JPA.domain.Tariff;
import rewardCalculation.JPA.repositories.RewardRepository;
import rewardCalculation.JPA.repositories.TariffRepository;
import rewardCalculation.dto.PaymentDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class CalculatePayment {

    private final TariffRepository tariffRepository;

    public List<PaymentDTO> calculate(List<Employee> employeeList,List<Reward> rewardList){
        log.info("{} start!",this.getClass().getSimpleName());
        List<Tariff> tariffs = tariffRepository.findAll();
        log.debug("Tariffs - {}",tariffs);
        List<PaymentDTO> paymentDTOList = new ArrayList<>();
        Map<Long, List<Reward>> rewardsByEmployee = rewardList.stream()
                .collect(Collectors.groupingBy(Reward::getEmployeeId));
        for (Employee employees :employeeList){
            List<Reward> rewards = rewardsByEmployee.getOrDefault(employees.getId(), List.of());
            if (rewards.isEmpty()) continue;
            for (Reward reward : rewards) {
                paymentDTOList.add(createPaymentDto(employees,reward,tariffs));
            }
        }
        log.debug("Successful create all paymentDTOS : {}", paymentDTOList);
        log.info("{} execute!",this.getClass().getSimpleName());
        return  paymentDTOList;

    }
    private PaymentDTO createPaymentDto(Employee employee , Reward reward, List<Tariff> tariffs){
        log.debug("Create paymentDTO!");
        log.debug("Get tariff by reward job type: {}",reward.getJobType());
        Optional<Tariff> tariff = tariffs.stream()
                .filter(t -> reward.getJobType().equals(t.getJobType()))
                .findFirst();
        log.debug("Tariff - {}", tariff);
        PaymentDTO paymentDTO =  PaymentDTO.builder()
                .employeeId(employee.getId())
                .amount(getAmount(employee.getBonusCoefficient(),tariff.get().getAmount())).build();
        log.info("Отправлен платеж: amount = {}, recipient = {}",paymentDTO.getAmount(), employee.getFirstName() + " " + employee.getLastName());
        return paymentDTO;
    }
    private BigDecimal getAmount(BigDecimal bonusCoefficient , BigDecimal amount){
        BigDecimal result = BigDecimal.ONE.add(bonusCoefficient).multiply(amount);
        return result.setScale(2, RoundingMode.HALF_UP);
    }
}
