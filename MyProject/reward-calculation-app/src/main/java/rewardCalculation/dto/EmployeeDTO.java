package rewardCalculation.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class EmployeeDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private BigDecimal bonusCoefficient;

}
