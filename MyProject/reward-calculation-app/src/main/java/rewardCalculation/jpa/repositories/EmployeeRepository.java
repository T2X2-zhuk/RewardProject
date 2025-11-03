package rewardCalculation.jpa.repositories;

import rewardCalculation.jpa.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
