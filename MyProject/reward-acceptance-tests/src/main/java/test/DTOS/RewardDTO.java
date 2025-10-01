package test.DTOS;

import lombok.*;
import test.DTOS.EnumObject.RewardStatus;

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
