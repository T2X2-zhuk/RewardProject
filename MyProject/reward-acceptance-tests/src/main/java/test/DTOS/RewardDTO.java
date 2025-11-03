package test.DTOS;

import lombok.*;
import test.DTOS.EnumObject.RewardStatus;

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
