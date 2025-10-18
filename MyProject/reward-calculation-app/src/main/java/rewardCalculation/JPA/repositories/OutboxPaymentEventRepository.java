package rewardCalculation.JPA.repositories;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import rewardCalculation.JPA.domain.OutboxPaymentEvent;

import java.time.LocalDateTime;
import java.util.List;

public interface OutboxPaymentEventRepository extends JpaRepository<OutboxPaymentEvent, Long> {

    @Modifying
    @Transactional
    @Query("""
       UPDATE OutboxPaymentEvent o
       SET o.status = :newStatus
       WHERE o.id = :id AND o.status IN ('PENDING', 'FAILED')
       """)
    int updateStatusIfPendingOrFailed(@Param("id") Long id, @Param("newStatus") String newStatus);

    @Query("""
    SELECT o FROM OutboxPaymentEvent o
    WHERE o.status IN :statuses
    ORDER BY o.createdAt ASC
""")
    List<OutboxPaymentEvent> findTopNByStatusesReadyForProcessing(
            @Param("statuses") List<String> statuses,
            Pageable pageable
    );

    @Modifying
    @Transactional
    @Query("""
   UPDATE OutboxPaymentEvent o
   SET o.status = 'FAILED'
   WHERE o.id = :id
""")
    int markAsFailed(@Param("id") Long id);
}
