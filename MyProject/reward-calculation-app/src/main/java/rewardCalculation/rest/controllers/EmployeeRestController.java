package rewardCalculation.rest.controllers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import rewardCalculation.requests.CommonRequestForEmployeeParameters;
import rewardCalculation.dto.EmployeeDTO;

import rewardCalculation.responses.CommonResponseForEmployeeParameters;
import rewardCalculation.services.employee.CreateEmployeeService;
import org.springframework.web.bind.annotation.*;
import rewardCalculation.services.employee.GetEmployeeService;

@RestController
@RequestMapping("/api/test/employee")
@RequiredArgsConstructor
@Slf4j
public class EmployeeRestController {

    private final GetEmployeeService getEmployeeService;
    private final CreateEmployeeService createEmployeeService;

    @PostMapping(path = "/createEmployee",
            consumes = "application/json",
            produces = "application/json")
    public CommonResponseForEmployeeParameters createEmployee(@RequestBody CommonRequestForEmployeeParameters request) {

        log.info("[{}] {} is start!", MDC.get("traceId"), this.getClass().getSimpleName());

        CommonResponseForEmployeeParameters response = createEmployeeService.execute(request);

        log.info("[{}] {} is execute!", MDC.get("traceId"), this.getClass().getSimpleName());

        return response;
    }

    @GetMapping(path = "/getEmployee/{id}",
            produces = "application/json")
    public CommonResponseForEmployeeParameters getEmployee(@PathVariable Long id) {

        log.info("[{}] {} is start!", MDC.get("traceId"), this.getClass().getSimpleName());

        CommonResponseForEmployeeParameters response = getEmployeeService.execute(CommonRequestForEmployeeParameters.builder()
                .employeeDTO(EmployeeDTO.builder().id(id).build()).build());

        log.info("[{}] {} is execute!", MDC.get("traceId"), this.getClass().getSimpleName());

        return response;
    }
}
