package RewardCalculation.service.interfaces;

import RewardCalculation.JPA.domain.Employee;
import RewardCalculation.responses.RewardCalculationResponse;
import RewardCalculation.util.ValidationError;

import java.util.List;

public interface RewardCalculationService {

   RewardCalculationResponse execute(List<Employee> employees);
}
