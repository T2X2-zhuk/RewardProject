package rewardCalculation.servises.employee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import rewardCalculation.cacheConfig.GetEmployeeUsingCache;
import rewardCalculation.cacheConfig.RedisCacheConfig;
import rewardCalculation.requests.CommonRequestForEmployeeParameters;
import rewardCalculation.responses.CommonResponseForEmployeeParameters;
import rewardCalculation.util.ValidationError;
import rewardCalculation.validations.validators.employee.CreateEmployeeRequestValidator;
import rewardCalculation.JPA.domain.Employee;
import rewardCalculation.JPA.repositories.EmployeeRepository;
import rewardCalculation.dto.EmployeeDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
class CreateEmployeeServiceImpl implements CreateEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final CreateEmployeeRequestValidator validator;
    private final GetEmployeeUsingCache getEmployeeUsingCache;

    public CommonResponseForEmployeeParameters execute(CommonRequestForEmployeeParameters request) {
        log.info("{} is start!", this.getClass().getSimpleName());
        CommonResponseForEmployeeParameters response = new CommonResponseForEmployeeParameters();
        List<ValidationError> errors = validator.validate(request);
        log.debug("Validation is execute!");
        if (errors.isEmpty()) {
            Employee employee = buildEmployee(request.getEmployeeDTO());
            saveEmployeeAndDeleteCacheEmployee(employee);
            log.debug("Employee : {} successful saved!",employee);
            setEmployeeDTO(response,employee);
        } else {
            response.setErrors(errors);
            log.warn("Validation failed errors : {}" , errors);
        }
        log.info("{} is execute!", this.getClass().getSimpleName());
        return response;
    }

    private void saveEmployeeAndDeleteCacheEmployee(Employee employee){
        employeeRepository.save(employee);
        getEmployeeUsingCache.clearEMPLOYEESCache();
    }
    private Employee buildEmployee(EmployeeDTO employeeDTO){
        return Employee.builder()
                .firstName(employeeDTO.getFirstName())
                .lastName(employeeDTO.getLastName())
                .bonusCoefficient(employeeDTO.getBonusCoefficient().setScale(2, RoundingMode.HALF_UP)).build();
    }
    private void setEmployeeDTO(CommonResponseForEmployeeParameters response, Employee employee){
        response.setEmployeeDTO(EmployeeDTO.builder().id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .bonusCoefficient(employee.getBonusCoefficient()).build());
    }
}
