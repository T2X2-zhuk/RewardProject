package rewardCalculation.servises.reward;

import lombok.RequiredArgsConstructor;
import rewardCalculation.JPA.domain.Reward;
import rewardCalculation.JPA.repositories.RewardRepository;
import rewardCalculation.dto.RewardDTO;
import rewardCalculation.requests.CommonRequestForRewardParameters;
import rewardCalculation.responses.CommonResponseForRewardParameters;
import rewardCalculation.util.ValidationError;
import rewardCalculation.validations.validators.reward.CreateRewardRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
class CreateRewardServiceImpl implements CreateRewardService {

    private final RewardRepository rewardRepository;
    private final CreateRewardRequestValidator validator;

    @Override
    public CommonResponseForRewardParameters execute(CommonRequestForRewardParameters request) {
        CommonResponseForRewardParameters response = new CommonResponseForRewardParameters();
        List<ValidationError> errors = validator.validate(request);
        if (errors.isEmpty()){
            Reward reward = Reward.builder()
                    .employeeId(request.getRewardDTO().getEmployeeId())
                    .jobType(request.getRewardDTO().getJobType())
                    .status(request.getRewardDTO().getStatus()).build();
            rewardRepository.save(reward);
            response.setRewardDTO(RewardDTO.builder()
                    .id(reward.getId())
                    .employeeId(reward.getEmployeeId())
                    .jobType(reward.getJobType()).build());
        }else {
            response.setErrors(errors);
        }
        return response;
    }
}
