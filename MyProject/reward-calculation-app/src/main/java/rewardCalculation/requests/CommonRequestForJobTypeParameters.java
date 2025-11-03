package rewardCalculation.requests;

import lombok.*;
import rewardCalculation.dto.JobTypeDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonRequestForJobTypeParameters {

    private JobTypeDTO jobTypeDTO;
}
