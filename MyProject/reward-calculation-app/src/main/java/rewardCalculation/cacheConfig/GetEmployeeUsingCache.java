package rewardCalculation.cacheConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import rewardCalculation.JPA.domain.Employee;
import rewardCalculation.JPA.repositories.EmployeeRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetEmployeeUsingCache {

    private final EmployeeRepository employeeRepository;

    @Cacheable(cacheNames = RedisCacheConfig.EMPLOYEES)
    public List<Employee> getAllEmployeesWithCache() {
        return employeeRepository.findAll();
    }
    @CacheEvict(value = RedisCacheConfig.EMPLOYEES,allEntries = true)
    public void clearEMPLOYEESCache(){

    }
}
