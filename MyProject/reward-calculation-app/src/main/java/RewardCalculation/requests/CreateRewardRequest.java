package RewardCalculation.requests;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRewardRequest {

    private Long employeeId;

    private String jobType;

    private String status;

}
