package rewardCalculation.dto;

import lombok.*;
import rewardCalculation.jpa.enums.RewardStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RewardDTO {

    private Long id;

    private Long employeeId;

    private String jobType;

    private RewardStatus status;

}
