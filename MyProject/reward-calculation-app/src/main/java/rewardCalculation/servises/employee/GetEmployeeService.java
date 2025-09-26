package rewardCalculation.servises.employee;

import rewardCalculation.requests.CommonRequestForEmployeeParameters;
import rewardCalculation.responses.CommonResponseForEmployeeParameters;

public interface GetEmployeeService {

    CommonResponseForEmployeeParameters execute(CommonRequestForEmployeeParameters request);

}
