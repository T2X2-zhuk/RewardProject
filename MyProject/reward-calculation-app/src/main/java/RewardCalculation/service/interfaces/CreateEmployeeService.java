package RewardCalculation.service.interfaces;

import RewardCalculation.dto.EmployeeDTO;
import RewardCalculation.requests.CreateEmployeeRequest;
import RewardCalculation.responses.CreateEmployeeResponse;

public interface CreateEmployeeService {

    CreateEmployeeResponse execute(CreateEmployeeRequest request);
}
