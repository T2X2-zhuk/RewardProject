package rewardCalculation.calculate;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@ToString
@Slf4j
public class CalculatePayment {

    private final TariffRepository tariffRepository;
    private final RewardRepository rewardRepository;

    public List<PaymentDTO> calculate(List<Employee> employees){
        log.info("{} start!" ,this);
        List<Tariff> tariffs = tariffRepository.findAll();
        log.debug("Tariffs - {}",tariffs);
        List<PaymentDTO> paymentDTOList = employees.stream()
                .flatMap(employee -> rewardRepository.findByEmployeeId(employee.getId()).stream()
                        .map(reward -> createPaymentDto(employee, reward, tariffs)))
                .toList();
        log.debug("Successful create all PaymentDTOS : {}", paymentDTOList);
        log.info("{} execute!" , this);
        return  paymentDTOList;

    }
    private PaymentDTO createPaymentDto(Employee employee , Reward reward,List<Tariff> tariffs){
        log.debug("Create paymentDTOs!");
        log.debug("Get tariff by reward job type: {}",reward.getJobType());
        Optional<Tariff> tariff = tariffs.stream()
                .filter(t -> reward.getJobType().equals(t.getJobType()))
                .findFirst();
        log.debug("Tariff - {}", tariff);
        PaymentDTO paymentDTO =  PaymentDTO.builder()
                .employeeId(employee.getId())
                .amount(getAmount(employee.getBonusCoefficient(),tariff.get().getAmount())).build();
        log.debug("Successful create paymentDTO: {}", paymentDTO);
        return paymentDTO;
    }
    private double getAmount(Double bonusCoefficient , Double amount){
        return (1 + bonusCoefficient) * amount;
    }
}
