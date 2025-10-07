package test.DTOS;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder
public class PaymentDTO {

    private Long id;

    private Long employeeId;

    private BigDecimal amount;

}
