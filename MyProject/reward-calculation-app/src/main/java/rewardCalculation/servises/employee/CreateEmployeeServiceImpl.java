package rewardCalculation.servises.employee;

import lombok.RequiredArgsConstructor;
import rewardCalculation.requests.CommonRequestForEmployeeParameters;
import rewardCalculation.responses.CommonResponseForEmployeeParameters;
import rewardCalculation.validations.validators.employee.CreateEmployeeRequestValidator;
import rewardCalculation.JPA.domain.Employee;
import rewardCalculation.JPA.repositories.EmployeeRepository;
import rewardCalculation.dto.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
@RequiredArgsConstructor
class CreateEmployeeServiceImpl implements CreateEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final CreateEmployeeRequestValidator validator;

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
