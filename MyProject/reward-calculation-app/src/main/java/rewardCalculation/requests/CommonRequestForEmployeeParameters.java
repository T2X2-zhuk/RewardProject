package rewardCalculation.requests;

import rewardCalculation.dto.EmployeeDTO;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonRequestForEmployeeParameters {

    private EmployeeDTO employeeDTO;
}
