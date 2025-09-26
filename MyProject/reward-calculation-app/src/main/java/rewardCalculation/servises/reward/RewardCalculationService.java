package rewardCalculation.servises.reward;

import rewardCalculation.restClientRewardPayment.RewardPaymentResponse;
import rewardCalculation.JPA.domain.Employee;

import java.util.List;

public interface RewardCalculationService {

   RewardPaymentResponse execute(List<Employee> employees);
}
