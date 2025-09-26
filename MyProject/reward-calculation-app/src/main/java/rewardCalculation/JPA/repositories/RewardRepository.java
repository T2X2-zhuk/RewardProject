package rewardCalculation.JPA.repositories;

import rewardCalculation.JPA.domain.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface RewardRepository extends JpaRepository<Reward, Long> {

    @Query("SELECT rd FROM Reward rd where rd.employeeId = :employeeId AND (rd.status IS NULL OR rd.status = '' OR rd.status != 'PAID')")
    List<Reward> findByEmployeeId(@Param("employeeId") Long employeeId);

    Optional<Reward> findByEmployeeIdAndJobType(Long employeeId,String jobType);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Reward rd SET rd.status = :status where rd.employeeId = :employeeId")
    void rewardSetStatus(@Param("status") String status, @Param("employeeId") Long employeeId);

}
