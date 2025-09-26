package rewardCalculation.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardDTO {

    private Long id;

    private Long employeeId;

    private String jobType;

    private String status;

}
