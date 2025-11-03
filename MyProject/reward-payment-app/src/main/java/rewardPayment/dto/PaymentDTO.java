package rewardPayment.dto;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

    private Long id;
    private Long employeeId;
    private Long rewardId;
    private BigDecimal amount;
}
