package rewardCalculation.transactionalOutBox.jpa.repositories;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import rewardCalculation.transactionalOutBox.jpa.enums.OutboxPaymentStatus;
import rewardCalculation.transactionalOutBox.jpa.domain.OutboxPaymentEvent;

import java.util.List;

public interface OutboxPaymentEventRepository extends JpaRepository<OutboxPaymentEvent, Long> {

    @Modifying
    @Transactional
    @Query("""
   UPDATE OutboxPaymentEvent o
   SET o.status = :newStatus
   WHERE o.id = :id AND o.status IN :allowedStatuses
""")
    int updateStatusIfPendingOrFailed(@Param("id") Long id,
                                      @Param("newStatus") OutboxPaymentStatus newStatus,
                                      @Param("allowedStatuses") List<OutboxPaymentStatus> allowedStatuses);

    @Query("""
    SELECT o FROM OutboxPaymentEvent o
    WHERE o.status IN :statuses
    ORDER BY o.createdAt ASC
""")
    List<OutboxPaymentEvent> findByStatusesReadyForProcessing(
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
            UPDATE OutboxPaymentEvent o SET o.status = 'FAILED' WHERE o.id IN :ids
            """)
    int markEventsAsFailed(@Param("ids") List<Long> ids);
}
