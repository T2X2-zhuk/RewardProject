package rewardCalculation.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import rewardCalculation.dto.JobTypeDTO;
import rewardCalculation.dto.RewardDTO;
import rewardCalculation.requests.CommonRequestForJobTypeParameters;
import rewardCalculation.requests.CommonRequestForRewardParameters;
import rewardCalculation.responses.CommonResponseForJobTypeParameters;
import rewardCalculation.responses.CommonResponseForRewardParameters;
import rewardCalculation.servises.jobType.CreateJobTypeService;
import rewardCalculation.servises.jobType.GetJobTypeService;

@RestController
@RequestMapping("/api/test/job/type")
@RequiredArgsConstructor
@Slf4j
public class JobTypeRestController {

    private final CreateJobTypeService createJobTypeService;
    private final GetJobTypeService getJobTypeService;
    @PostMapping(path = "/createJobType",
            consumes = "application/json",
            produces = "application/json")
    public CommonResponseForJobTypeParameters createJobType(@RequestBody CommonRequestForJobTypeParameters request) {
        log.info("{} is start!",this.getClass().getSimpleName());
        CommonResponseForJobTypeParameters response = createJobTypeService.execute(request);
        log.info("{} is execute!",this.getClass().getSimpleName());
        return response;
    }

    @GetMapping(path = "/getJobType/{id}",
            produces = "application/json")
    public CommonResponseForJobTypeParameters getJobType(@PathVariable Long id) {
        log.info("{} is start!",this.getClass().getSimpleName());
        CommonRequestForJobTypeParameters request = CommonRequestForJobTypeParameters.builder().jobTypeDTO(JobTypeDTO.builder().id(id).build()).build();
        CommonResponseForJobTypeParameters response = getJobTypeService.execute(request);
        log.info("{} is execute!",this.getClass().getSimpleName());
        return response;
    }
}
