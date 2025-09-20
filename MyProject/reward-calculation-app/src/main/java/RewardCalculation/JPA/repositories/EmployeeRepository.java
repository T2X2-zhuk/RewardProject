package RewardCalculation.JPA.repositories;

import RewardCalculation.JPA.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {


    Optional<Employee> findByFirstNameAndLastName(String firstName,String lastName);
}
