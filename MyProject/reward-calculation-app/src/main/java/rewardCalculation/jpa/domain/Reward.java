package rewardCalculation.jpa.domain;

import jakarta.persistence.*;
import lombok.*;
import rewardCalculation.jpa.enums.RewardStatus;

@Entity
@Table(name = "rewards")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reward {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employeeId;

    @ManyToOne
    @JoinColumn(name = "job_type_id", nullable = false)
    private JobTypeEntity jobType;

    @Enumerated(EnumType.STRING)
    @Column(name = "reward_status",nullable = false)
    private RewardStatus status;

}
