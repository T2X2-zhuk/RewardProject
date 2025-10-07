package rewardCalculation.calculate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.JPA.domain.EnumObject.RewardStatus;
import rewardCalculation.dto.PaymentDTO;
import rewardCalculation.JPA.domain.Reward;
import rewardCalculation.JPA.repositories.RewardRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SuccessfulPayment {

    private final RewardRepository rewardRepository;

    public void execute(List<PaymentDTO> paymentDTOS) {
        log.info("{} start!",this.getClass().getSimpleName());
        List<Reward> rewards = paymentDTOS.stream()
                .flatMap(dto -> rewardRepository.findByEmployeeIdAndStatus(dto.getEmployeeId(), RewardStatus.UNPAID).stream())
                .toList();
        rewardSetStatusOnPaid(rewards);
        log.info("{} execute!",this.getClass().getSimpleName());
    }
    private void rewardSetStatusOnPaid(List<Reward> rewardsAfterPayment) {
        rewardsAfterPayment.stream()
                .map(Reward::getEmployeeId)
                .distinct()
                .forEach(empId -> rewardRepository.rewardSetStatus(RewardStatus.PAID, empId));
    }
}
