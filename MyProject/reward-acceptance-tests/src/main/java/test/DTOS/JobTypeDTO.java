package test.DTOS;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class JobTypeDTO {

    private Long id;
    private String jobType;
}
