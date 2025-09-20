package RewardCalculation.service.interfaces;

import RewardCalculation.responses.GetEmployeeResponse;
import RewardCalculation.util.ValidationError;

import java.util.List;

public interface GetEmployeeService {

    GetEmployeeResponse execute(Long id);

}
