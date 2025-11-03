package test.rewardapp.requests;

import lombok.*;
import test.DTOS.JobTypeDTO;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonRequestForJobTypeParameters {

    private JobTypeDTO jobTypeDTO;
}
