package RewardCalculation.responses;

import RewardCalculation.dto.EmployeeDTO;
import RewardCalculation.util.CoreResponse;
import RewardCalculation.util.ValidationError;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetEmployeeResponse extends CoreResponse {

    private EmployeeDTO employeeDTO;

    public GetEmployeeResponse(List<ValidationError> errors) {
        super(errors);
    }
}
