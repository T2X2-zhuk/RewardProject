package rewardCalculation.calculate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.JPA.domain.Employee;
import rewardCalculation.JPA.domain.Reward;
import rewardCalculation.JPA.domain.Tariff;
import rewardCalculation.JPA.repositories.TariffRepository;
import rewardCalculation.dto.PaymentDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class CalculatePayment {

    private final TariffRepository tariffRepository;

    public List<PaymentDTO> calculate(List<Employee> employeeList,List<Reward> rewardList){
        log.info("{} start!",this.getClass().getSimpleName());
        Map<String, Tariff> tariffByJobType = getTariffsByJobType();
        log.debug("Tariffs - {}",tariffByJobType);
        List<PaymentDTO> paymentDTOList = new ArrayList<>();
        Map<Long, List<Reward>> rewardsByEmployee = rewardList.stream()
                .collect(Collectors.groupingBy(Reward::getEmployeeId));
        employeeList.forEach(employee -> {
            List<Reward> rewards = rewardsByEmployee.get(employee.getId());
            if (rewards != null) {
                rewards.forEach(reward ->
                        paymentDTOList.add(createPaymentDto(employee, reward, tariffByJobType))
                );
            }
        });
        log.debug("Successful create all paymentDTOS : {}", paymentDTOList);
        log.info("{} execute!",this.getClass().getSimpleName());
        return  paymentDTOList;

    }
    private PaymentDTO createPaymentDto(Employee employee , Reward reward,  Map<String, Tariff> tariffByJobType){
        log.debug("Create paymentDTO!");
        log.debug("Get tariff by reward job type: {}",reward.getJobType());
        Tariff tariff = tariffByJobType.get(reward.getJobType());
        log.debug("Tariff - {}", tariff);
        PaymentDTO paymentDTO =  PaymentDTO.builder()
                .employeeId(employee.getId())
                .amount(getAmount(employee.getBonusCoefficient(),tariff.getAmount())).build();
        log.info("Отправлен платеж: amount = {}, recipient = {}",paymentDTO.getAmount(), employee.getFirstName() + " " + employee.getLastName());
        return paymentDTO;
    }
    private BigDecimal getAmount(BigDecimal bonusCoefficient , BigDecimal amount){
        BigDecimal result = BigDecimal.ONE.add(bonusCoefficient).multiply(amount);
        return result.setScale(2, RoundingMode.HALF_UP);
    }

    private Map<String, Tariff> getTariffsByJobType() {
        List<Tariff> tariffs = tariffRepository.findAll();
        return tariffs.stream().collect(Collectors.toMap(Tariff::getJobType, t -> t));
    }
}
