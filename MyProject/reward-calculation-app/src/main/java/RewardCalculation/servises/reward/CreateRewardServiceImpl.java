package RewardCalculation.servises.reward;

import RewardCalculation.JPA.domain.Reward;
import RewardCalculation.JPA.repositories.RewardRepository;
import RewardCalculation.dto.RewardDTO;
import RewardCalculation.requests.CommonRequestForRewardParameters;
import RewardCalculation.responses.CommonResponseForRewardParameters;
import RewardCalculation.util.ValidationError;
import RewardCalculation.validations.validators.reward.CreateRewardRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
class CreateRewardServiceImpl implements CreateRewardService {

    @Autowired
    private RewardRepository rewardRepository;
    @Autowired private CreateRewardRequestValidator validator;

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
