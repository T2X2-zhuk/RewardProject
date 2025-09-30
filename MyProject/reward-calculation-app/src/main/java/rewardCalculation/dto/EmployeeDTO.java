package rewardCalculation.dto;

import lombok.*;

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

    private Double bonusCoefficient;

}
