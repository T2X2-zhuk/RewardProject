package rewardCalculation.rest.cleandb;
import lombok.RequiredArgsConstructor;
import rewardCalculation.JPA.repositories.EmployeeRepository;
import rewardCalculation.JPA.repositories.RewardRepository;
import rewardCalculation.JPA.repositories.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/rewardDb")
@RequiredArgsConstructor
public class CleanRewardDbController {

    private final EmployeeRepository employeeRepository;
    private final RewardRepository rewardRepository;
    private final TariffRepository tariffRepository;

    @PostMapping(path = "/cleanDb",
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
