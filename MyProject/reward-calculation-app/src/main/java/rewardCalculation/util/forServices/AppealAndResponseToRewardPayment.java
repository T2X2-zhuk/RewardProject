package rewardCalculation.util.forServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rewardCalculation.JPA.domain.Employee;
import rewardCalculation.JPA.domain.EnumObject.RewardStatus;
import rewardCalculation.JPA.domain.Reward;
import rewardCalculation.JPA.repositories.RewardRepository;
import rewardCalculation.calculate.CalculatePayment;
import rewardCalculation.calculate.SendPaymentsToRewardPaymentApplication;
import rewardCalculation.restClientRewardPayment.RewardPaymentResponse;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AppealAndResponseToRewardPayment {

    private final RewardRepository rewardRepository;
    private final SendPaymentsToRewardPaymentApplication sendingToMicroservice;
    private final CalculatePayment calculatePayment;

    public RewardPaymentResponse successful(List<Employee> employees, List<Reward> rewardList) {
        List<Reward> unpaidRewards = rewardList.stream()
                .filter(r -> r.getStatus() == RewardStatus.UNPAID)
                .toList();
        List<Long> unpaidRewardsIds = unpaidRewards.stream()
                .map(Reward::getId)
                .toList();

        RewardPaymentResponse paymentResponse = sendingToMicroservice.send(
                calculatePayment.calculate(employees, unpaidRewards)
        );
        log.debug("Payment sent successfully!");
        // Обновление статуса в зависимости от результата
        updateRewardStatuses(unpaidRewardsIds,paymentResponse);

        return paymentResponse;
    }

    @Transactional
    protected void updateRewardStatuses(List<Long> unpaidRewardsIds, RewardPaymentResponse paymentResponse) {
        rewardRepository.rewardSetStatusForList(
                paymentResponse.hasErrors() || !paymentResponse.isSuccessfulSaving()
                        ? RewardStatus.PROCESSING
                        : RewardStatus.PAID,
                unpaidRewardsIds
        );
        log.debug("Rewards updated to {}",
                paymentResponse.hasErrors() ? RewardStatus.PROCESSING : RewardStatus.PAID);
    }

}
