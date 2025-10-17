package rewardCalculation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RewardCalculationApplication {

    public static void main(String[] args) {
        SpringApplication.run(RewardCalculationApplication.class, args);
    }

}
