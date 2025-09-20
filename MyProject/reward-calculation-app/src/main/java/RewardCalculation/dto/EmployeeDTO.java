package RewardCalculation.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private Double bonusCoefficient;

}
