package RewardCalculation.service.interfaces;

import RewardCalculation.requests.CreateEmployeeRequest;
import RewardCalculation.requests.CreateTariffRequest;
import RewardCalculation.responses.CreateEmployeeResponse;
import RewardCalculation.responses.CreateTariffResponse;

public interface CreateTariffService {

    CreateTariffResponse execute(CreateTariffRequest request);
}
