package rewardCalculation.calculate;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.dto.PaymentDTO;
import rewardCalculation.JPA.domain.Reward;
import rewardCalculation.JPA.repositories.RewardRepository;
import rewardCalculation.restClientRewardPayment.RewardPaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@ToString
@Slf4j
public class SuccessfulPayment {

    private final RewardRepository rewardRepository;

    public RewardPaymentResponse execute(RewardPaymentResponse paymentResponse) {
        log.info("{} start!", this);
        List<Reward> rewards = paymentResponse.getPaymentDTOS().stream()
                .map(PaymentDTO::getEmployeeId)
                .map(rewardRepository::findByEmployeeId)
                .flatMap(List::stream)
                .toList();
        log.debug("Get all rewards");
        rewardSetStatusOnPaid(rewards, paymentResponse.getStatus());
        log.debug("For each reward {} is established : {}",rewards,paymentResponse.getStatus());
        log.info("{} execute!" , this);
        return paymentResponse;
    }
    private void rewardSetStatusOnPaid(List<Reward> rewardsAfterPayment, String status) {
        rewardsAfterPayment.forEach(
                reward -> rewardRepository.rewardSetStatus(status, reward.getEmployeeId())
        );
    }
}
