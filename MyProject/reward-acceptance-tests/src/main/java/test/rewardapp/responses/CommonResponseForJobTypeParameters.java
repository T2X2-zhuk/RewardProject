package test.rewardapp.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import test.DTOS.JobTypeDTO;
import test.utils.CoreResponse;
import test.utils.ValidationError;

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
