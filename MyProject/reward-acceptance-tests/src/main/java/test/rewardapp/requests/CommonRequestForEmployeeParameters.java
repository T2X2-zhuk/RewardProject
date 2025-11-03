package test.rewardapp.requests;

import lombok.*;
import test.DTOS.EmployeeDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonRequestForEmployeeParameters {

    private EmployeeDTO employeeDTO;
}
