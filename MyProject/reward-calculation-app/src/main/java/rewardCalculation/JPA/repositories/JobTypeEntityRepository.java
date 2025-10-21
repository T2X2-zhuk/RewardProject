package rewardCalculation.JPA.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rewardCalculation.JPA.domain.JobTypeEntity;

import java.util.Optional;

public interface JobTypeEntityRepository extends JpaRepository<JobTypeEntity, Long> {

    Optional<JobTypeEntity> findByJobType(String jobType);
}
