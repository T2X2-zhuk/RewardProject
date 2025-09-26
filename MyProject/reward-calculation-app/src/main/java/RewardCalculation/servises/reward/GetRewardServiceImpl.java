package RewardCalculation.servises.reward;

import RewardCalculation.JPA.domain.Reward;
import RewardCalculation.JPA.repositories.RewardRepository;
import RewardCalculation.dto.RewardDTO;
import RewardCalculation.requests.CommonRequestForRewardParameters;
import RewardCalculation.responses.CommonResponseForRewardParameters;
import RewardCalculation.util.ValidationError;
import RewardCalculation.validations.validators.reward.GetRewardRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
class GetRewardServiceImpl implements GetRewardService {

    @Autowired private GetRewardRequestValidator validator;
    @Autowired private RewardRepository rewardRepository;

    @Override
    public CommonResponseForRewardParameters execute(CommonRequestForRewardParameters request) {
        CommonResponseForRewardParameters response = new CommonResponseForRewardParameters();
        List<ValidationError> errors = validator.validate(request.getRewardDTO().getId());
        if (errors.isEmpty()){
            Optional<Reward> reward = rewardRepository.findById(request.getRewardDTO().getId());
            response.setRewardDTO(RewardDTO.builder().id(reward.get().getId())
                    .employeeId(reward.get().getEmployeeId())
                    .jobType(reward.get().getJobType())
                    .status(reward.get().getStatus()).build());
        }else {
            response.setErrors(errors);
        }
        return response;
    }
}
