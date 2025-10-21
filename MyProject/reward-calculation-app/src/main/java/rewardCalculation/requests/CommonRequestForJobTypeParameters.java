package rewardCalculation.requests;

import lombok.*;
import rewardCalculation.dto.JobTypeDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonRequestForJobTypeParameters {

    private JobTypeDTO jobTypeDTO;
}
