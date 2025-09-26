package RewardCalculation.responses;

import RewardCalculation.dto.EmployeeDTO;
import RewardCalculation.util.CoreResponse;
import RewardCalculation.util.ValidationError;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponseForEmployeeParameters extends CoreResponse {

    private EmployeeDTO employeeDTO;

    public CommonResponseForEmployeeParameters(List<ValidationError> errors) {
        super(errors);
    }
}
