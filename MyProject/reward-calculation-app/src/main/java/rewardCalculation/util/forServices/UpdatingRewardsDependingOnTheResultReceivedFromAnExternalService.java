package rewardCalculation.util.forServices;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import rewardCalculation.EnumObject.RewardStatus;
import rewardCalculation.JPA.domain.Reward;
import rewardCalculation.JPA.repositories.RewardRepository;
import rewardCalculation.restClientRewardPayment.RewardPaymentResponse;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(
        prefix = "app",
        name = "transactionalOutBox",
        havingValue = "false",
        matchIfMissing = true
)
public class UpdatingRewardsDependingOnTheResultReceivedFromAnExternalService {

    private final RewardRepository rewardRepository;

    @Transactional
    public RewardPaymentResponse result(RewardPaymentResponse response,List<Reward> rewards){
        log.info("{} is start!",this.getClass().getSimpleName());
        if (!response.isSuccessfulSaving() || response.hasErrors()){
            log.error("Rewards is not updated!");
        }else {
            List<Long> rewardIds = rewards.stream().map(Reward::getId).collect(Collectors.toList());
            rewardRepository.rewardSetStatusForList(RewardStatus.PAID,rewardIds);
            log.info("All is OK");
        }
        return response;
    }
}
