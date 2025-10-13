package rewardCalculation.servises.reward;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.JPA.domain.EnumObject.RewardStatus;
import rewardCalculation.JPA.domain.Reward;
import rewardCalculation.JPA.repositories.RewardRepository;
import rewardCalculation.dto.RewardDTO;
import rewardCalculation.requests.CommonRequestForRewardParameters;
import rewardCalculation.responses.CommonResponseForRewardParameters;
import rewardCalculation.util.forError.ValidationError;
import rewardCalculation.validations.validators.reward.CreateRewardRequestValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
class CreateRewardServiceImpl implements CreateRewardService {

    private final RewardRepository rewardRepository;
    private final CreateRewardRequestValidator validator;

    @Override
    public CommonResponseForRewardParameters execute(CommonRequestForRewardParameters request) {
        log.info("{} is start!", this.getClass().getSimpleName());
        CommonResponseForRewardParameters response = new CommonResponseForRewardParameters();
        List<ValidationError> errors = validator.validate(request);
        log.debug("Validation is execute!");
        if (errors.isEmpty()){
            Reward reward = Reward.builder()
                    .employeeId(request.getRewardDTO().getEmployeeId())
                    .jobType(request.getRewardDTO().getJobType().toUpperCase())
                    .status(RewardStatus.UNPAID).build();
            rewardRepository.save(reward);
            log.debug("Reward saved: {}", reward);
            response.setRewardDTO(RewardDTO.builder()
                    .id(reward.getId())
                    .employeeId(reward.getEmployeeId())
                    .jobType(reward.getJobType())
                    .status(reward.getStatus()).build());
        }else {
            response.setErrors(errors);
            log.warn("Validation failed errors : {}" , errors);
        }
        log.info("{} is execute!", this.getClass().getSimpleName());
        return response;
    }
}
