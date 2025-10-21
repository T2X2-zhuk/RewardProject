package rewardCalculation.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import rewardCalculation.dto.JobTypeDTO;
import rewardCalculation.util.forError.CoreResponse;
import rewardCalculation.util.forError.ValidationError;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponseForJobTypeParameters extends CoreResponse {

    private JobTypeDTO jobTypeDTO;

    public CommonResponseForJobTypeParameters(List<ValidationError> errors) {
        super(errors);
    }
}
