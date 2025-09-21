package RewardCalculation.service.servises;

import RewardCalculation.JPA.domain.Reward;
import RewardCalculation.JPA.repositories.RewardRepository;
import RewardCalculation.dto.RewardDTO;
import RewardCalculation.requests.CreateRewardRequest;
import RewardCalculation.responses.CreateRewardResponse;
import RewardCalculation.service.interfaces.CreateRewardService;
import RewardCalculation.util.ValidationError;
import RewardCalculation.validations.validators.CreateRewardRequestValidator;
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

    public CreateRewardResponse execute(CreateRewardRequest request) {
        CreateRewardResponse response = new CreateRewardResponse();
        List<ValidationError> errors = validator.validate(request);
        if (errors.isEmpty()){
            Reward reward = Reward.builder()
                    .employeeId(request.getRewardDTO().getEmployeeId())
                    .jobType(request.getRewardDTO().getJobType())
                    .status(request.getRewardDTO().getStatus()).build();
            rewardRepository.save(reward);
            response.setRewardDTO(request.getRewardDTO());
        }else {
            response.setErrors(errors);
        }
        return response;
    }
}
