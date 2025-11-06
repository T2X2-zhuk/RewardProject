package rewardCalculation.transactionalOutBox.jpa.domain;

import jakarta.persistence.*;
import lombok.*;
import rewardCalculation.transactionalOutBox.jpa.enums.OutboxPaymentStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "outbox_payment_event")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxPaymentEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Таблица для rewardIds
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "outbox_payment_reward_ids",
            joinColumns = @JoinColumn(name = "outbox_id")
    )
    @Column(name = "reward_id")
    private List<Long> rewardIds;
    @Lob
    @Column(name = "payload",nullable = false, columnDefinition = "LONGTEXT")
    private String payload; // JSON с данными платежа

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private OutboxPaymentStatus status; // PENDING, FAILED

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

}
