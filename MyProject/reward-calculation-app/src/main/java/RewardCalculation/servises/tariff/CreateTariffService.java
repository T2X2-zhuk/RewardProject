package RewardCalculation.servises.tariff;
import RewardCalculation.requests.CommonRequestForTariffParameters;
import RewardCalculation.responses.CommonResponseForTariffParameters;

public interface CreateTariffService {

    CommonResponseForTariffParameters execute(CommonRequestForTariffParameters request);
}
