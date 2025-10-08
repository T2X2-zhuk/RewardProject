package rewardCalculation.JPA.repositories;

import rewardCalculation.JPA.domain.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TariffRepository extends JpaRepository<Tariff, Long> {

    Optional<Tariff> findByJobType(String jobType);

    @Query("SELECT tr.jobType FROM Tariff tr")
    Set<String> findAllJobTypes();
}
