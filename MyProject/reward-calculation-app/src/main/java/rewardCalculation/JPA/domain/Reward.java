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

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "job_type", nullable = false)
    private String jobType;

    @Enumerated(EnumType.STRING)
    @Column(name = "reward_status",nullable = false)
    private RewardStatus status;

}
