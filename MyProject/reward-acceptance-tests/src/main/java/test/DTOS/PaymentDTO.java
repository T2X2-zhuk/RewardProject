package test.DTOS;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder
public class PaymentDTO {

    private Long id;

    private Long employeeId;

    private Double amount;

}
