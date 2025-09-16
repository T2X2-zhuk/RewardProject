package RewardCalculation.validations.MethodsValidatorClasses;

import RewardCalculation.JPA.domain.Employee;
import RewardCalculation.JPA.domain.Reward;
import RewardCalculation.JPA.repositories.RewardRepository;
import RewardCalculation.JPA.repositories.TariffRepository;
import RewardCalculation.util.ValidationErrorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
@Component
 public class ValidatorClassWithMethodsForJobTypes {
    @Autowired
    private ValidationErrorFactory errorFactory;
    @Autowired
    private TariffRepository tariffRepository;
    @Autowired private RewardRepository rewardRepository;

    public Optional<ValidationErrorDTO> isSuchJobTypesInDatabase(List<Employee> employees){
        List<String> jobTypes = tariffRepository.findAllJobTypes();
        ValidationErrorDTO errorDTO = new ValidationErrorDTO();
        if (!employees.isEmpty()){
            for (Employee employee : employees) {
                List<Reward> rewards = rewardRepository.findByEmployeeId(employee.getId());
                for (Reward reward : rewards) {
                    if (!jobTypes.contains(reward.getJobType())){
                       errorDTO = errorFactory.buildError("ERROR_CODE_2" + " " + jobTypes);
                    }
                }
            }
        }else {
            errorDTO = errorFactory.buildError("ERROR_CODE_1");
        }
        return Optional.of(errorDTO);
    }

}
