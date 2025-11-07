package rewardCalculation.cache.get;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import rewardCalculation.jpa.domain.Employee;
import rewardCalculation.jpa.repositories.EmployeeRepository;
import rewardCalculation.cache.config.RedisCacheConfig;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetEmployeeUsingCache {

    private final EmployeeRepository employeeRepository;

    @Cacheable(cacheNames = RedisCacheConfig.EMPLOYEE_CACHE)
    public List<Employee> getAllEmployeesWithCache() {

        return employeeRepository.findAll();
    }

    @CacheEvict(value = RedisCacheConfig.EMPLOYEE_CACHE,allEntries = true)
    public void clearEMPLOYEESCache(){

    }
}
