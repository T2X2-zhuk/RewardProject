package RewardCalculation.service.interfaces;

import RewardCalculation.responses.GetEmployeeResponse;
import RewardCalculation.responses.GetTariffResponse;

public interface GetTariffService {

    GetTariffResponse execute(Long id);

}
