package rewardCalculation.requests;
import rewardCalculation.dto.TariffDTO;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonRequestForTariffParameters {

    private TariffDTO tariffDTO;
}
