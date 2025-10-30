package rewardCalculation.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PaymentDTO {

    private Long id;
    private Long employeeId;
    private Long rewardId;
    private BigDecimal amount;
}
