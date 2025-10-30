package rewardCalculation.util.forServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.JPA.domain.Reward;
import rewardCalculation.JPA.domain.Tariff;
import rewardCalculation.cache.get.GetTariffUsingCache;
import rewardCalculation.dto.PaymentDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class CalculatePayment {

    private final GetTariffUsingCache getTariffsByJobType;

    public List<PaymentDTO> calculate(List<Reward> rewardList){
        log.info("{} start!",this.getClass().getSimpleName());
        Map<String, Tariff> tariffByJobType = getTariffsByJobType.getTariffsByJobType();
        log.debug("Tariffs - {}",tariffByJobType);
        List<PaymentDTO> paymentDTOList = new ArrayList<>();
        for (Reward reward : rewardList){
            paymentDTOList.add(createPaymentDto(reward,tariffByJobType));
        }
        log.debug("Successful create all paymentDTOS : {}", paymentDTOList);
        log.info("{} execute!",this.getClass().getSimpleName());
        return  paymentDTOList;
    }
    private PaymentDTO createPaymentDto(Reward reward,Map<String, Tariff> tariffByJobType){
        log.debug("Create paymentDTO!");
        log.debug("Get tariff by reward job type: {}",reward.getJobType());
        Tariff tariff = tariffByJobType.get(reward.getJobType().getJobType());
        log.debug("Tariff - {}", tariff);
        PaymentDTO paymentDTO =  PaymentDTO.builder()
                .employeeId(reward.getEmployeeId().getId()).rewardId(reward.getId())
                .amount(getAmount(reward.getEmployeeId().getBonusCoefficient(),tariff.getAmount())).build();
        log.info("Отправлен платеж: amount = {}, recipient = {}",paymentDTO.getAmount(), reward.getEmployeeId().getFirstName() + " " + reward.getEmployeeId().getLastName());
        return paymentDTO;
    }
    private BigDecimal getAmount(BigDecimal bonusCoefficient , BigDecimal amount){

        return BigDecimal.ONE.add(bonusCoefficient).multiply(amount);
    }
}
