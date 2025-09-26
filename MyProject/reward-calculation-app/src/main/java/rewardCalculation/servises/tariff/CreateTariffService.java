package rewardCalculation.servises.tariff;
import rewardCalculation.responses.CommonResponseForTariffParameters;
import rewardCalculation.requests.CommonRequestForTariffParameters;

public interface CreateTariffService {

    CommonResponseForTariffParameters execute(CommonRequestForTariffParameters request);
}
