package rewardCalculation.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TariffDTO {

    private Long id;
    private String jobType;
    private Double amount;
}
