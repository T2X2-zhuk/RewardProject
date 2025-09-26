package RewardCalculation.servises.employee;

import RewardCalculation.requests.CommonRequestForEmployeeParameters;
import RewardCalculation.responses.CommonResponseForEmployeeParameters;

public interface GetEmployeeService {

    CommonResponseForEmployeeParameters execute(CommonRequestForEmployeeParameters request);

}
