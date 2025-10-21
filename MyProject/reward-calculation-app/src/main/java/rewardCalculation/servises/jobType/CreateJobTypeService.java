package rewardCalculation.servises.jobType;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rewardCalculation.JPA.domain.JobTypeEntity;
import rewardCalculation.JPA.repositories.JobTypeEntityRepository;
import rewardCalculation.dto.JobTypeDTO;
import rewardCalculation.requests.CommonRequestForJobTypeParameters;
import rewardCalculation.responses.CommonResponseForJobTypeParameters;
import rewardCalculation.util.forError.ValidationError;
import rewardCalculation.validations.validators.jobType.CreateJobTypeRequestValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateJobTypeService {

    private final CreateJobTypeRequestValidator validator;
    private final JobTypeEntityRepository repository;

    @Transactional
    public CommonResponseForJobTypeParameters execute(CommonRequestForJobTypeParameters request){
        log.info("{} is start!", this.getClass().getSimpleName());
        CommonResponseForJobTypeParameters response = new CommonResponseForJobTypeParameters();
        List<ValidationError> errors = validator.validate(request);
        log.debug("Validation is execute with result {}!", errors);
        if (errors.isEmpty()){
            JobTypeEntity jobType = repository.save(JobTypeEntity.builder().jobType(request.getJobTypeDTO().getJobType().toUpperCase()).build());
            response.setJobTypeDTO(JobTypeDTO.builder().id(jobType.getId()).jobType(jobType.getJobType()).build());
            log.debug("Save Job type is execute with result successful! {}", jobType);
        }else {
            log.warn("Validation failed errors : {}" , errors);
            response.setErrors(errors);
        }
        log.info("{} is execute finished!", this.getClass().getSimpleName());
        return response;
    }

}
