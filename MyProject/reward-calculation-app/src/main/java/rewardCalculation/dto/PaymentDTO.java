package rewardCalculation.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PaymentDTO {

    private Long id;
    private Long employeeId;
    private Double amount;
}
