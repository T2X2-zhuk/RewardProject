package rewardCalculation.JPA.repositories;

import rewardCalculation.EnumObject.RewardStatus;
import rewardCalculation.JPA.domain.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface RewardRepository extends JpaRepository<Reward, Long> {

    @Query("SELECT rd FROM Reward rd where rd.employeeId.id = :employeeId AND rd.jobType.jobType = :jobType AND rd.status = :status")
    Optional<Reward> findByEmployeeIdAndJobTypeAndStatus(@Param("employeeId") Long employeeId, @Param("jobType") String jobType,@Param("status") RewardStatus status);

    List<Reward> findTop15ByStatus(RewardStatus status);

    @Transactional
    @Modifying
    @Query("UPDATE Reward rв SET rв.status = :status WHERE rв.id IN :ids")
    void rewardSetStatusForList(@Param("status") RewardStatus status, @Param("ids") List<Long> ids);
}
