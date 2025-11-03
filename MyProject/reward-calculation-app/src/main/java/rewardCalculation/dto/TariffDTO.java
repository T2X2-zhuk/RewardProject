package rewardCalculation.dto;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TariffDTO {

    private Long id;
    private String jobType;
    private BigDecimal amount;
}
