package rewardCalculation.responses;


import lombok.*;
import rewardCalculation.util.forErrors.CoreResponse;
import rewardCalculation.dto.TariffDTO;
import rewardCalculation.util.forErrors.ValidationError;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class CommonResponseForTariffParameters extends CoreResponse {

    private TariffDTO tariffDTO;

    public CommonResponseForTariffParameters(List<ValidationError> errors) {
        super(errors);
    }
}
