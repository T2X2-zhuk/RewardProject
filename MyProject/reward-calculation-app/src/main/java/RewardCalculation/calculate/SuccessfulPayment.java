package RewardCalculation.calculate;

import RewardCalculation.JPA.domain.Reward;
import RewardCalculation.JPA.repositories.RewardRepository;
import RewardCalculation.dto.PaymentDTO;
import RewardCalculation.restClientRewardPayment.RewardPaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SuccessfulPayment {


    @Autowired private RewardRepository rewardRepository;

    public RewardPaymentResponse execute(RewardPaymentResponse paymentResponse) {
        List<Reward> rewards = paymentResponse.getPaymentDTOS().stream()
                .map(PaymentDTO::getEmployeeId)
                .map(rewardRepository::findByEmployeeId)
                .flatMap(List::stream)
                .toList();
        rewardSetStatusOnPaid(rewards, paymentResponse.getStatus());
        return paymentResponse;
    }
    private void rewardSetStatusOnPaid(List<Reward> rewardsAfterPayment, String status) {
        rewardsAfterPayment.forEach(
                reward -> rewardRepository.rewardSetStatus(status, reward.getEmployeeId())
        );
    }
}
