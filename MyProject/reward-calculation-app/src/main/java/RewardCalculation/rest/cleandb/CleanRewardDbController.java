package RewardCalculation.rest.cleandb;
import RewardCalculation.JPA.repositories.EmployeeRepository;
import RewardCalculation.JPA.repositories.RewardRepository;
import RewardCalculation.JPA.repositories.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/cleanDb")
public class CleanRewardDbController {

    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private RewardRepository rewardRepository;
    @Autowired private TariffRepository tariffRepository;

    @PostMapping(path = "/",
            consumes = "application/json",
            produces = "application/json")
    public CleanRewardDbResponse cleanDb(@RequestBody CleanRewardDbRequest request) {
        CleanRewardDbResponse response = new CleanRewardDbResponse();

        if (request.isCleanTariff()) {
            tariffRepository.deleteAll();
            response.setTariffDeleted(true);
        }
        if (request.isCleanReward()) {
            rewardRepository.deleteAll();
            response.setRewardDeleted(true);
        }
        if (request.isCleanEmployee()) {
            employeeRepository.deleteAll();
            response.setEmployeeDeleted(true);
        }
        return response;
    }

}
