package test.rewardapp.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import test.DTOS.EmployeeDTO;
import test.utils.CoreResponse;
import test.utils.ValidationError;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponseForEmployeeParameters extends CoreResponse {

    private EmployeeDTO employeeDTO;

    public CommonResponseForEmployeeParameters(List<ValidationError> errors) {
        super(errors);
    }
}
