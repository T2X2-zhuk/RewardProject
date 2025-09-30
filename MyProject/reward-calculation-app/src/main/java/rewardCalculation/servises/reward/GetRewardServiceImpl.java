package rewardCalculation.servises.reward;

import lombok.RequiredArgsConstructor;
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
class GetRewardServiceImpl implements GetRewardService {

    private final GetRewardRequestValidator validator;
    private final RewardRepository rewardRepository;

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
