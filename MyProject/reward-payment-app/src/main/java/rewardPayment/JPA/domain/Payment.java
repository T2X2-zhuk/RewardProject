package rewardPayment.JPA.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
public class Payment {

    @Id
    private Long id;

    @Column("employee_id")
    private Long employeeId;

    @Column("reward_id")
    private Long rewardId;

    @Column("amount")
    private BigDecimal amount;

}
