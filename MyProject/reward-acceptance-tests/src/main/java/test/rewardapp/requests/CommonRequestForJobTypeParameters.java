package test.rewardapp.requests;

import lombok.*;
import test.DTOS.JobTypeDTO;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonRequestForJobTypeParameters {

    private JobTypeDTO jobTypeDTO;
}
