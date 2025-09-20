package RewardCalculation.requests;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateEmployeeRequest {

    private String firstName;

    private String lastName;

    private Double bonusCoefficient;

}
