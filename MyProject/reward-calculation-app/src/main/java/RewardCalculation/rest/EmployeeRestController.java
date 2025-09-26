package RewardCalculation.rest;
import RewardCalculation.dto.EmployeeDTO;
import RewardCalculation.requests.CommonRequestForEmployeeParameters;

import RewardCalculation.responses.CommonResponseForEmployeeParameters;
import RewardCalculation.servises.employee.CreateEmployeeService;
import RewardCalculation.servises.employee.GetEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test/employee")
public class EmployeeRestController {

    @Autowired private GetEmployeeService getEmployeeService;
    @Autowired private CreateEmployeeService createEmployeeService;
    @PostMapping(path = "/createEmployee",
            consumes = "application/json",
            produces = "application/json")
    public CommonResponseForEmployeeParameters createEmployee(@RequestBody CommonRequestForEmployeeParameters request) {
        return createEmployeeService.execute(request);
    }

    @GetMapping(path = "/getEmployee/{id}",
            produces = "application/json")
    public CommonResponseForEmployeeParameters getEmployee(@PathVariable Long id) {
        CommonRequestForEmployeeParameters request = CommonRequestForEmployeeParameters.builder()
                .employeeDTO(EmployeeDTO.builder().id(id).build()).build();
        return getEmployeeService.execute(request);
    }
}
