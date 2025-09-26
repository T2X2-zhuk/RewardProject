package RewardCalculation.requests;
import RewardCalculation.dto.TariffDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonRequestForTariffParameters {

    private TariffDTO tariffDTO;
}
