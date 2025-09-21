package RewardCalculation.requests;

import RewardCalculation.JPA.domain.Tariff;
import RewardCalculation.dto.TariffDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTariffRequest {

    private TariffDTO tariffDTO;
}
