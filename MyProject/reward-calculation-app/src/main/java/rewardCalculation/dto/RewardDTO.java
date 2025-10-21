package rewardCalculation.dto;

import lombok.*;
import rewardCalculation.EnumObject.RewardStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RewardDTO {

    private Long id;

    private Long employeeId;

    private String jobType;

    private RewardStatus status;

}
