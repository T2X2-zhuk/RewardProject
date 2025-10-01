package rewardCalculation.validations.MethodsValidatorClasses;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.JPA.domain.Employee;
import rewardCalculation.JPA.domain.EnumObject.RewardStatus;
import rewardCalculation.JPA.domain.Reward;
import rewardCalculation.JPA.repositories.RewardRepository;
import rewardCalculation.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_For_Reward_2"));
            log.debug("Error : {}",error);
            return error;
        }
        return Optional.empty();
    }

    public Optional<ValidationError> isSuchRewardById(Long rewardId){

        if (rewardRepository.findById(rewardId).isEmpty()){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_For_Reward_4"));
            log.debug("Error : {}",error);
            return error;
        }

        return Optional.empty();
    }
    public Optional<ValidationError> rewardIdIsNull(Long rewardId){

        if (rewardId == null){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_For_Reward_5"));
            log.debug("Error : {}",error);
            return error;
        }

        return Optional.empty();
    }
    public Optional<ValidationError> suchRewardIsDatabase(Long employeeId,String jobType){

        if (rewardRepository.findByEmployeeIdAndJobType(employeeId,jobType).isPresent()){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_For_Reward_3"));
            log.debug("Error : {}",error);
            return error;
        }

        return Optional.empty();
    }

    public Optional<ValidationError> isRewardsForThisEmployees(List<Employee> employees){
        List<Reward> rewards = new ArrayList<>();
        for (Employee employee : employees){
            rewards.addAll(rewardRepository.findByEmployeeId(employee.getId(), RewardStatus.UNPAID));
        }
        if (rewards.isEmpty()){
            Optional<ValidationError> error = Optional.of(errorFactory.buildError("ERROR_CODE_For_Reward_1"));
            log.debug("Error : {}",error);
            return error;
        }

        return Optional.empty();
    }
}
