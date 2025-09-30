package rewardCalculation.rest;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.requests.CommonRequestForEmployeeParameters;
import rewardCalculation.dto.EmployeeDTO;

import rewardCalculation.responses.CommonResponseForEmployeeParameters;
import rewardCalculation.servises.employee.CreateEmployeeService;
import rewardCalculation.servises.employee.GetEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test/employee")
@RequiredArgsConstructor
@ToString
@Slf4j
public class EmployeeRestController {

    private final GetEmployeeService getEmployeeService;
    private final CreateEmployeeService createEmployeeService;
    @PostMapping(path = "/createEmployee",
            consumes = "application/json",
            produces = "application/json")
    public CommonResponseForEmployeeParameters createEmployee(@RequestBody CommonRequestForEmployeeParameters request) {
        log.info("{} is start!",this);
        CommonResponseForEmployeeParameters response = createEmployeeService.execute(request);
        log.info("{} is execute!",this);
        return response;
    }

    @GetMapping(path = "/getEmployee/{id}",
            produces = "application/json")
    public CommonResponseForEmployeeParameters getEmployee(@PathVariable Long id) {
        log.info("{} is start!",this);
        CommonRequestForEmployeeParameters request = CommonRequestForEmployeeParameters.builder()
                .employeeDTO(EmployeeDTO.builder().id(id).build()).build();
        CommonResponseForEmployeeParameters response = getEmployeeService.execute(request);
        log.info("{} is execute!",this);
        return response;
    }
}
