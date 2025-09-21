package RewardCalculation.rest.reward;

import RewardCalculation.JPA.repositories.EmployeeRepository;
import RewardCalculation.responses.RewardCalculationResponse;
import RewardCalculation.service.interfaces.RewardCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reward/calculation")
public class RewardCalculationRestController {

    @Autowired private EmployeeRepository repository;
    @Autowired private RewardCalculationService rewardCalculationService;

    @PostMapping(path = "/",
            produces = "application/json")
    public RewardCalculationResponse execute() {
       return rewardCalculationService.execute(repository.findAll());
    }

}
