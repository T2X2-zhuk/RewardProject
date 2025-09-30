package rewardCalculation.requests;

import rewardCalculation.dto.EmployeeDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CommonRequestForEmployeeParameters {

    private EmployeeDTO employeeDTO;
}
