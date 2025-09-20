package RewardCalculation.service.servises;

import RewardCalculation.JPA.domain.Employee;
import RewardCalculation.JPA.repositories.EmployeeRepository;
import RewardCalculation.dto.EmployeeDTO;
import RewardCalculation.requests.CreateEmployeeRequest;
import RewardCalculation.responses.CreateEmployeeResponse;
import RewardCalculation.service.interfaces.CreateEmployeeService;
import RewardCalculation.validations.validators.CreateEmployeeRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
class CreateEmployeeServiceImpl implements CreateEmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired private CreateEmployeeRequestValidator validator;
    @Override
    public CreateEmployeeResponse execute(CreateEmployeeRequest request) {
        CreateEmployeeResponse response = new CreateEmployeeResponse();
        if (validator.validate(request).isEmpty()){
            EmployeeDTO employeeDTO = createEmployeeDTO(storeEmployeeToDatabase(request));
            response.setEmployeeDTO(employeeDTO);
        }else {
            response.setErrors(validator.validate(request));
        }
        return response;
    }

    private Employee storeEmployeeToDatabase(CreateEmployeeRequest request) {
        Employee employee = Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .bonusCoefficient(request.getBonusCoefficient()).build();
        return employeeRepository.save(employee);
    }

    private EmployeeDTO createEmployeeDTO(Employee employee) {
        return EmployeeDTO.builder().id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .bonusCoefficient(employee.getBonusCoefficient()).build();
    }
}
