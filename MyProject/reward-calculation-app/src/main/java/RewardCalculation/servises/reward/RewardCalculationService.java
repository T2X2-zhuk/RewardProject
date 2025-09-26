package RewardCalculation.servises.reward;

import RewardCalculation.JPA.domain.Employee;
import RewardCalculation.restClientRewardPayment.RewardPaymentResponse;

import java.util.List;

public interface RewardCalculationService {

   RewardPaymentResponse execute(List<Employee> employees);
}
