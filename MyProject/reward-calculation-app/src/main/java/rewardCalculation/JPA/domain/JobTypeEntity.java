package rewardCalculation.JPA.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "job_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class JobTypeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_type", nullable = false)
    private String jobType;
}
