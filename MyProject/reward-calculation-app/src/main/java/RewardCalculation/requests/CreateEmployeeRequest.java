package RewardCalculation.requests;

import RewardCalculation.dto.EmployeeDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateEmployeeRequest {

    private EmployeeDTO employeeDTO;
}
