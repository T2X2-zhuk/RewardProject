package rewardCalculation.servises.reward;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.dto.RewardDTO;
import rewardCalculation.responses.CommonResponseForRewardParameters;
import rewardCalculation.validations.validators.reward.GetRewardRequestValidator;
import rewardCalculation.JPA.domain.Reward;
import rewardCalculation.JPA.repositories.RewardRepository;
import rewardCalculation.requests.CommonRequestForRewardParameters;
import rewardCalculation.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
class GetRewardServiceImpl implements GetRewardService {

    private final GetRewardRequestValidator validator;
    private final RewardRepository rewardRepository;

    @Override
    public CommonResponseForRewardParameters execute(CommonRequestForRewardParameters request) {
        log.info("{} is start!", this.getClass().getSimpleName());
        CommonResponseForRewardParameters response = new CommonResponseForRewardParameters();
        List<ValidationError> errors = validator.validate(request.getRewardDTO().getId());
        log.debug("Validation is execute!");
        if (errors.isEmpty()){
            Optional<Reward> reward = rewardRepository.findById(request.getRewardDTO().getId());
            response.setRewardDTO(RewardDTO.builder().id(reward.get().getId())
                    .employeeId(reward.get().getEmployeeId())
                    .jobType(reward.get().getJobType())
                    .status(reward.get().getStatus()).build());
            log.debug("Reward found: {}", reward);
        }else {
            response.setErrors(errors);
            log.warn("Validation failed errors : {}" , errors);
        }
        log.info("{} is execute!", this.getClass().getSimpleName());
        return response;
    }
}
