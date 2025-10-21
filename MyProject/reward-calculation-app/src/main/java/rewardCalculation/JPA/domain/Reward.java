package rewardCalculation.JPA.domain;

import jakarta.persistence.*;
import lombok.*;
import rewardCalculation.JPA.domain.EnumObject.RewardStatus;

@Entity
@Table(name = "rewards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Reward {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employeeId;

    @ManyToOne
    @JoinColumn(name = "job_type_id", nullable = false)
    private JobTypeEntity jobType;

    @Enumerated(EnumType.STRING)
    @Column(name = "reward_status",nullable = false)
    private RewardStatus status;

}
