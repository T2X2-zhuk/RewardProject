package rewardCalculation.jpa.repositories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import rewardCalculation.jpa.enums.RewardStatus;
import rewardCalculation.jpa.domain.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RewardRepository extends JpaRepository<Reward, Long> {

    @Query("SELECT rd FROM Reward rd where rd.employeeId.id = :employeeId AND rd.jobType.jobType = :jobType AND rd.status = :status")
    Optional<Reward> findByEmployeeIdAndJobTypeAndStatus(@Param("employeeId") Long employeeId, @Param("jobType") String jobType, @Param("status") RewardStatus status);

    @EntityGraph(attributePaths = {"employeeId"})
    @Query("SELECT r FROM Reward r WHERE r.status = :status")
    List<Reward> findByStatusWithEmployee(@Param("status") RewardStatus status, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Reward rd SET rd.status = :status WHERE rd.id IN :ids")
    void rewardSetStatusForList(@Param("status") RewardStatus status, @Param("ids") List<Long> ids);

}
