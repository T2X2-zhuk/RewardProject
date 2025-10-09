package rewardCalculation.servises.employee;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import rewardCalculation.cacheConfig.GetEmployeeUsingCache;
import rewardCalculation.requests.CommonRequestForEmployeeParameters;
import rewardCalculation.responses.CommonResponseForEmployeeParameters;
import rewardCalculation.validations.validators.employee.GetEmployeeValidator;
import rewardCalculation.JPA.domain.Employee;
import rewardCalculation.JPA.repositories.EmployeeRepository;
import rewardCalculation.dto.EmployeeDTO;
import rewardCalculation.util.ValidationError;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
class GetEmployeeServiceImpl implements GetEmployeeService {

     private final EmployeeRepository repository;
     private final GetEmployeeValidator validator;
     private final GetEmployeeUsingCache getEmployeeUsingCache;

    public CommonResponseForEmployeeParameters execute(CommonRequestForEmployeeParameters request){
        log.info("{} is start!", this.getClass().getSimpleName());
        CommonResponseForEmployeeParameters response = new CommonResponseForEmployeeParameters();
        List<ValidationError> errors = validator.validate(request.getEmployeeDTO().getId());
        log.debug("Validation is execute!");
        if (!errors.isEmpty()){
            log.warn("Validation failed errors : {}" , errors);
            response.setErrors(errors);
        }else {
            getEmployeeById(request.getEmployeeDTO().getId()).ifPresent(employee ->
                    response.setEmployeeDTO(createEmployeeDTO(employee))
            );   log.info("Employee : {} was successful received", response.getEmployeeDTO());
        }
        log.info("{} is execute!", this.getClass().getSimpleName());
        return response;
    }
    private Optional<Employee> getEmployeeById(Long id){
        return getEmployeeUsingCache.getAllEmployeesWithCache().stream()
                .filter(employee1 -> employee1.getId().equals(id)).findFirst();
    }
    private EmployeeDTO createEmployeeDTO(Employee employee) {
        return EmployeeDTO.builder().id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .bonusCoefficient(employee.getBonusCoefficient()).build();
    }
}
