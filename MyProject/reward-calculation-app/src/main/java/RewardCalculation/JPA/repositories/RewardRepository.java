package RewardCalculation.JPA.repositories;

import RewardCalculation.JPA.domain.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RewardRepository extends JpaRepository<Reward, Long> {

    List<Reward> findByEmployeeId(Long employeeId);

}
