package rewardCalculation.servises.reward;

import rewardCalculation.JPA.domain.Reward;
import rewardCalculation.restClientRewardPayment.RewardPaymentResponse;
import rewardCalculation.JPA.domain.Employee;

import java.util.List;

public interface RewardCalculationService {

   RewardPaymentResponse execute(List<Employee> employees,List<Reward> rewardList);
}
