package rewardCalculation.validations.MethodsValidatorClasses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.EnumObject.RewardStatus;
import rewardCalculation.JPA.domain.Reward;
import rewardCalculation.JPA.repositories.RewardRepository;
import rewardCalculation.util.forErrors.ValidationError;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
@Component
@RequiredArgsConstructor
@Slf4j
 public class ValidatorClassWithMethodsForReward {

    private final ValidationErrorFactory errorFactory;
    private final RewardRepository rewardRepository;

    public Optional<ValidationError> employeeIdMustNotBeEmpty(Long employeeId){
        if (employeeId == null){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_FOR_REWARD_2"));
            log.debug("Error : {}",error);
            return error;
        }
        return Optional.empty();
    }

    public Optional<ValidationError> isSuchRewardById(Long rewardId){
        if (rewardRepository.findById(rewardId).isEmpty()){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_FOR_REWARD_4"));
            log.debug("Error : {}",error);
            return error;
        }
        return Optional.empty();
    }

    public Optional<ValidationError> suchRewardIsDatabase(Long employeeId,String jobType){
        Optional<Reward> reward = rewardRepository.findByEmployeeIdAndJobTypeAndStatus(employeeId,jobType.toUpperCase(),RewardStatus.UNPAID);
        if (reward.isPresent()){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_FOR_REWARD_3"));
            log.debug("Error : {}",error);
            return error;
        }
        return Optional.empty();
    }

    public Optional<ValidationError> isRewardsForThisEmployees(List<Reward> rewardList){
        if (rewardList.isEmpty()){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_FOR_REWARD_1"));
            log.debug("Error : {}",error);
            return error;
        }
        return Optional.empty();
    }

}
