package RewardCalculation.service.calculate;

import RewardCalculation.JPA.domain.Reward;
import RewardCalculation.JPA.repositories.RewardRepository;
import RewardCalculation.dto.PaymentDTO;
import RewardCalculation.responses.RewardCalculationResponse;
import RewardCalculation.restClientRewardPayment.RewardPaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SuccessfulPayment {


    @Autowired private RewardRepository rewardRepository;

    public RewardCalculationResponse execute(RewardCalculationResponse coreResponse,
                        RewardPaymentResponse paymentResponse) {
        coreResponse.setMessage("Ответ успешен - " + paymentResponse.getStatus());

        List<Reward> rewards = paymentResponse.getPaymentDTOS().stream()
                .map(PaymentDTO::getEmployeeId)
                .map(rewardRepository::findByEmployeeId)
                .flatMap(List::stream)
                .toList();
        rewardSetStatusOnPaid(rewards, paymentResponse.getStatus());

        return coreResponse;
    }
    private void rewardSetStatusOnPaid(List<Reward> rewardsAfterPayment, String status) {
        rewardsAfterPayment.forEach(
                reward -> rewardRepository.rewardSetStatus(status, reward.getEmployeeId())
        );
    }
}
