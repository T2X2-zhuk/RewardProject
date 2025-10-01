package test.rewardapp.requests;

import lombok.*;

import test.DTOS.TariffDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonRequestForTariffParameters {

    private TariffDTO tariffDTO;
}
