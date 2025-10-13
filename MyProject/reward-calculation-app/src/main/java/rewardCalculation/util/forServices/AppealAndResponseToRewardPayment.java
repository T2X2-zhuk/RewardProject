package rewardCalculation.util.forServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
        changesTheStatusOfAwardsThatPreviouslyHadTheStatusPROCESSING(rewardList);

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
        rewardRepository.rewardSetStatusForList(
                paymentResponse.hasErrors() || !paymentResponse.isSuccessfulSaving()
                        ? RewardStatus.PROCESSING
                        : RewardStatus.PAID,
                unpaidRewardsIds
        );

        if (paymentResponse.hasErrors()) {
            log.warn("Payment response has errors: {}", paymentResponse.getErrors());
        } else {
            log.debug("Rewards set to PAID");
            log.info("Execute!");
        }

        return paymentResponse;
    }

    private void changesTheStatusOfAwardsThatPreviouslyHadTheStatusPROCESSING(List<Reward> rewardList){
        List<Reward> processingRewards = rewardList.stream()
                .filter(r -> r.getStatus() == RewardStatus.PROCESSING)
                .toList();
        List<Long> processingRewardsIds = processingRewards.stream()
                .map(Reward::getId)
                .toList();
        if (!processingRewardsIds.isEmpty()) {
            rewardRepository.rewardSetStatusForList(RewardStatus.PAID, processingRewardsIds);
            log.info("Processing rewards set to PAID: {}", processingRewardsIds);
        } else {
            log.debug("No rewards with status PROCESSING found.");
        }
    }

}
