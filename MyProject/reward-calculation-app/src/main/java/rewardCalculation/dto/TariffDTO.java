package rewardCalculation.dto;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TariffDTO {

    private Long id;
    private String jobType;
    private BigDecimal amount;
}
