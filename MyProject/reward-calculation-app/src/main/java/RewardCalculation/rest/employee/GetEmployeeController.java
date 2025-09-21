package RewardCalculation.rest.employee;

import RewardCalculation.JPA.domain.Employee;
import RewardCalculation.JPA.repositories.EmployeeRepository;
import RewardCalculation.dto.EmployeeDTO;
import RewardCalculation.responses.GetEmployeeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/employee")
public class GetEmployeeController {

    @Autowired private EmployeeRepository employeeRepository;

    @GetMapping(path = "/{id}",
            produces = "application/json")
    public GetEmployeeResponse getEmployee(@PathVariable Long id) {


        return employeeRepository.findById(id)
                .map(this::createEmployeeDTO)
                .map(GetEmployeeResponse::new)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found by id " + id));
    }

    private EmployeeDTO createEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setLastName(employee.getLastName());
        employeeDTO.setBonusCoefficient(employee.getBonusCoefficient());
        return employeeDTO;
    }

}
