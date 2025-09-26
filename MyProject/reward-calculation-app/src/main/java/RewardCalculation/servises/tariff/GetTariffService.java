package RewardCalculation.servises.tariff;

import RewardCalculation.requests.CommonRequestForTariffParameters;
import RewardCalculation.responses.CommonResponseForTariffParameters;


public interface GetTariffService {

    CommonResponseForTariffParameters execute(CommonRequestForTariffParameters request);

}
