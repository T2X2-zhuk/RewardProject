package rewardCalculation.JPA.repositories;

import rewardCalculation.JPA.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
