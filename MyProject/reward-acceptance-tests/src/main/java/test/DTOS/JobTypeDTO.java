package test.DTOS;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobTypeDTO {

    private Long id;
    private String jobType;
}
