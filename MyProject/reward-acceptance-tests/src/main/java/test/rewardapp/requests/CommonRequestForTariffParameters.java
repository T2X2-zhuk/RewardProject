package test.rewardapp.requests;

import lombok.*;

import test.DTOS.TariffDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonRequestForTariffParameters {

    private TariffDTO tariffDTO;
}
