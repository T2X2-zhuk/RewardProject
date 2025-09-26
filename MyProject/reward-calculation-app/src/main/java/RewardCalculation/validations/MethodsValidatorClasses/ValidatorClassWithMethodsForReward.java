package RewardCalculation.validations.MethodsValidatorClasses;
import RewardCalculation.JPA.domain.Employee;
import RewardCalculation.JPA.domain.Reward;
import RewardCalculation.JPA.repositories.RewardRepository;
import RewardCalculation.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Component
 public class ValidatorClassWithMethodsForReward {

    @Autowired
    private ValidationErrorFactory errorFactory;
    @Autowired private RewardRepository rewardRepository;

    public Optional<ValidationError> employeeIdMustNotBeEmpty(Long employeeId){
        return (employeeId == null)
                ? Optional.of(errorFactory.buildError("ERROR_CODE_For_Reward_2"))
                : Optional.empty();
    }

    public Optional<ValidationError> isSuchRewardById(Long rewardId){
        return (rewardRepository.findById(rewardId).isEmpty())
                ? Optional.of(errorFactory.buildError("ERROR_CODE_For_Reward_4"))
                : Optional.empty();
    }
    public Optional<ValidationError> rewardIdIsNull(Long rewardId){
        return (rewardId == null)
                ? Optional.of(errorFactory.buildError("ERROR_CODE_For_Reward_5"))
                : Optional.empty();
    }
    public Optional<ValidationError> suchRewardIsDatabase(Long employeeId,String jobType){
        return (rewardRepository.findByEmployeeIdAndJobType(employeeId,jobType).isPresent())
                ? Optional.of(errorFactory.buildError("ERROR_CODE_For_Reward_3"))
                : Optional.empty();
    }

    public Optional<ValidationError> isRewardsForThisEmployees(List<Employee> employees){
        List<Reward> rewards = new ArrayList<>();
        for (Employee employee : employees){
            rewards.addAll(rewardRepository.findByEmployeeId(employee.getId()));
        }
        return (rewards.isEmpty())
                ? Optional.of(errorFactory.buildError("ERROR_CODE_For_Reward_1"))
                : Optional.empty();
    }
}
