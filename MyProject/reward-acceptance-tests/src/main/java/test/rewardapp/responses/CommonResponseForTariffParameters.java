package test.rewardapp.responses;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import test.DTOS.TariffDTO;
import test.utils.CoreResponse;
import test.utils.ValidationError;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponseForTariffParameters extends CoreResponse {

    private TariffDTO tariffDTO;

    public CommonResponseForTariffParameters(List<ValidationError> errors) {
        super(errors);
    }
}
