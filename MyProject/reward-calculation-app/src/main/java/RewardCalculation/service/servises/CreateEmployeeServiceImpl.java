package RewardCalculation.service.servises;

import RewardCalculation.JPA.domain.Employee;
import RewardCalculation.JPA.repositories.EmployeeRepository;
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
        if (validator.validate(request).isEmpty()) {
            Employee employee = Employee.builder()
                    .firstName(request.getEmployeeDTO().getFirstName())
                    .lastName(request.getEmployeeDTO().getLastName())
                    .bonusCoefficient(request.getEmployeeDTO().getBonusCoefficient()).build();
            employeeRepository.save(employee);
            response.setEmployeeDTO(request.getEmployeeDTO());
        } else {
            response.setErrors(validator.validate(request));
        }
        return response;
    }
}
