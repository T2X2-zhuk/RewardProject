package RewardCalculation.servises.employee;

import RewardCalculation.requests.CommonRequestForEmployeeParameters;
import RewardCalculation.responses.CommonResponseForEmployeeParameters;

public interface CreateEmployeeService {

    CommonResponseForEmployeeParameters execute(CommonRequestForEmployeeParameters request);
}
