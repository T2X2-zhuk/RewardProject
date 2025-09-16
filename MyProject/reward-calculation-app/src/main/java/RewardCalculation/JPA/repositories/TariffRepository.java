package RewardCalculation.JPA.repositories;

import RewardCalculation.JPA.domain.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TariffRepository extends JpaRepository<Tariff, Long> {

    Optional<Tariff> findByJobType(String jobType);

    @Query("SELECT tr.jobType FROM Tariff tr")
    List<String> findAllJobTypes();
}
