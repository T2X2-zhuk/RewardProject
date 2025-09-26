package rewardCalculation.servises.tariff;

import rewardCalculation.responses.CommonResponseForTariffParameters;
import rewardCalculation.requests.CommonRequestForTariffParameters;


public interface GetTariffService {

    CommonResponseForTariffParameters execute(CommonRequestForTariffParameters request);

}
