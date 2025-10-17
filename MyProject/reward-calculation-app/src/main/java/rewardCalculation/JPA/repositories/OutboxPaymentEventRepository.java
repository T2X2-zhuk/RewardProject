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

    @Query("SELECT o FROM OutboxPaymentEvent o WHERE o.status = :status ORDER BY o.createdAt ASC")
    List<OutboxPaymentEvent> findTopNByStatus(@Param("status") String status, Pageable pageable);

    @Query("SELECT o FROM OutboxPaymentEvent o WHERE o.status IN :statuses ORDER BY o.createdAt ASC")
    List<OutboxPaymentEvent> findTopNByStatuses(@Param("statuses") List<String> statuses, Pageable pageable);

    default List<OutboxPaymentEvent> findTopNByStatus(String status, int n) {
        return findTopNByStatus(status, PageRequest.of(0, n));
    }

    @Modifying
    @Query("DELETE FROM OutboxPaymentEvent o WHERE o.status = :status AND o.sentAt < :before")
    int deleteByStatusAndSentBefore(@Param("status") String status, @Param("before") LocalDateTime before);

    @Query("""
    SELECT o FROM OutboxPaymentEvent o
    WHERE o.status IN :statuses AND (o.nextAttemptAt IS NULL OR o.nextAttemptAt <= :now)
    ORDER BY o.createdAt ASC
""")
    List<OutboxPaymentEvent> findTopNByStatusesReadyForProcessing(
            @Param("statuses") List<String> statuses,
            @Param("now") LocalDateTime now,
            Pageable pageable
    );
}
