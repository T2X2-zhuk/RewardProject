package rewardCalculation.services.reward;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.jpa.enums.RewardStatus;
import rewardCalculation.jpa.domain.Reward;
import rewardCalculation.jpa.repositories.EmployeeRepository;
import rewardCalculation.jpa.repositories.JobTypeEntityRepository;
import rewardCalculation.jpa.repositories.RewardRepository;
import rewardCalculation.dto.RewardDTO;
import rewardCalculation.requests.CommonRequestForRewardParameters;
import rewardCalculation.responses.CommonResponseForRewardParameters;
import rewardCalculation.util.forErrors.ValidationError;
import rewardCalculation.validations.validators.reward.CreateRewardRequestValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateRewardService {

    private final RewardRepository rewardRepository;
    private final EmployeeRepository employeeRepository;
    private final CreateRewardRequestValidator validator;
    private final JobTypeEntityRepository jobTypeEntityRepository;

    @Transactional
    public CommonResponseForRewardParameters execute(CommonRequestForRewardParameters request) {
        log.info("{} is start!", this.getClass().getSimpleName());

        CommonResponseForRewardParameters response = new CommonResponseForRewardParameters();

        List<ValidationError> errors = validator.validate(request);
        log.debug("Validation is execute!");

        if (errors.isEmpty()){
            hearts(request,response);
        }else {
            response.setErrors(errors);
            log.warn("Validation failed errors : {}" , errors);
        }

        log.info("{} is execute!", this.getClass().getSimpleName());
        return response;
    }
    private void hearts(CommonRequestForRewardParameters request,CommonResponseForRewardParameters response){
        Reward reward = Reward.builder()
                .employeeId(employeeRepository.findById(request.getRewardDTO().getEmployeeId()).get())
                .jobType(jobTypeEntityRepository.findByJobType(request.getRewardDTO().getJobType().toUpperCase()).get())
                .status(RewardStatus.UNPAID).build();

        rewardRepository.save(reward);
        log.debug("Reward saved: {}", reward);

        response.setRewardDTO(RewardDTO.builder()
                .id(reward.getId())
                .employeeId(reward.getEmployeeId().getId())
                .jobType(reward.getJobType().getJobType())
                .status(reward.getStatus()).build());
    }
}
