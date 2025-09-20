package RewardCalculation.rest.controllers;
import RewardCalculation.requests.CreateEmployeeRequest;
import RewardCalculation.responses.CreateEmployeeResponse;
import RewardCalculation.service.interfaces.CreateEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/employee")
public class CreateEmployeeRestController {

    @Autowired private CreateEmployeeService service;

    @PostMapping(path = "/createEmployee",
            consumes = "application/json",
            produces = "application/json")
    public CreateEmployeeResponse createEmployee(@RequestBody CreateEmployeeRequest request) {
        return service.execute(request);
    }
}
