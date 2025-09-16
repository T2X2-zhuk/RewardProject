package RewardCalculation.rest;

import RewardCalculation.service.RewardCalculationService;
import RewardCalculation.JPA.domain.Employee;
import RewardCalculation.JPA.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reward/calculation")
public class RewardCalculationController {

    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private RewardCalculationService rewardCalculationService;

    @PostMapping(path = "/",
            produces = "application/json")
    public RewardCalculationResponse calculateRewards() {
        List<Employee> employees = employeeRepository.findAll();
        rewardCalculationService.execute(employees);
        return new RewardCalculationResponse();
    }

}
