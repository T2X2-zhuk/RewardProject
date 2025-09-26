package rewardCalculation.requests;
import rewardCalculation.dto.TariffDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonRequestForTariffParameters {

    private TariffDTO tariffDTO;
}
