package test.rewardapp.requests;

import lombok.*;
import test.DTOS.EmployeeDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonRequestForEmployeeParameters {

    private EmployeeDTO employeeDTO;
}
