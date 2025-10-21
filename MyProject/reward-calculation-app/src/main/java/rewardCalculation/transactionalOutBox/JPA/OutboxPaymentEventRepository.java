package rewardCalculation.transactionalOutBox.JPA;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import rewardCalculation.EnumObject.OutboxPaymentStatus;
import rewardCalculation.transactionalOutBox.domain.OutboxPaymentEvent;

import java.util.List;

public interface OutboxPaymentEventRepository extends JpaRepository<OutboxPaymentEvent, Long> {

    @Modifying
    @Transactional
    @Query("""
       UPDATE OutboxPaymentEvent o
       SET o.status = :newStatus
       WHERE o.id = :id AND o.status IN ('PENDING', 'FAILED')
       """)
    int updateStatusIfPendingOrFailed(@Param("id") Long id, @Param("newStatus") OutboxPaymentStatus newStatus);

    @Query("""
    SELECT o FROM OutboxPaymentEvent o
    WHERE o.status IN :statuses
    ORDER BY o.createdAt ASC
""")
    List<OutboxPaymentEvent> findTopNByStatusesReadyForProcessing(
            @Param("statuses") List<OutboxPaymentStatus> statuses,
            Pageable pageable
    );

    @Modifying
    @Transactional
    @Query("""
   UPDATE OutboxPaymentEvent o
   SET o.status = 'FAILED'
   WHERE o.id = :id
   """)
    void markAsFailed(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("""
           UPDATE OutboxPaymentEvent o SET o.status = 'FAILED' WHERE o.id IN :ids\s
          \s""")
    int markEventsAsFailed(@Param("ids") List<Long> ids);
}
