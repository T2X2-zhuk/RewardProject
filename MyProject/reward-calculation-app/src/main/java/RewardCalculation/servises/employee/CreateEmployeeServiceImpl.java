package RewardCalculation.servises.employee;

import RewardCalculation.JPA.domain.Employee;
import RewardCalculation.JPA.repositories.EmployeeRepository;
import RewardCalculation.dto.EmployeeDTO;
import RewardCalculation.requests.CommonRequestForEmployeeParameters;
import RewardCalculation.responses.CommonResponseForEmployeeParameters;
import RewardCalculation.validations.validators.employee.CreateEmployeeRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
class CreateEmployeeServiceImpl implements CreateEmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired private CreateEmployeeRequestValidator validator;

    public CommonResponseForEmployeeParameters execute(CommonRequestForEmployeeParameters request) {
        CommonResponseForEmployeeParameters response = new CommonResponseForEmployeeParameters();
        if (validator.validate(request).isEmpty()) {
            Employee employee = Employee.builder()
                    .firstName(request.getEmployeeDTO().getFirstName())
                    .lastName(request.getEmployeeDTO().getLastName())
                    .bonusCoefficient(request.getEmployeeDTO().getBonusCoefficient()).build();
            employeeRepository.save(employee);
            response.setEmployeeDTO(EmployeeDTO.builder().id(employee.getId())
                    .firstName(employee.getFirstName())
                    .lastName(employee.getLastName())
                    .bonusCoefficient(employee.getBonusCoefficient()).build());
        } else {
            response.setErrors(validator.validate(request));
        }
        return response;
    }
}
