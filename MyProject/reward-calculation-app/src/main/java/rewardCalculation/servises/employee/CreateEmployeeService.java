package rewardCalculation.servises.employee;

import rewardCalculation.requests.CommonRequestForEmployeeParameters;
import rewardCalculation.responses.CommonResponseForEmployeeParameters;

public interface CreateEmployeeService {

    CommonResponseForEmployeeParameters execute(CommonRequestForEmployeeParameters request);
}
