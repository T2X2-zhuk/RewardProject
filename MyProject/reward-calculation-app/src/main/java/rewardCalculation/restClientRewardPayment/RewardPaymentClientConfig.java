package rewardCalculation.restClientRewardPayment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RewardPaymentClientConfig {

    @Value( "${reward.payment.api.url}" )
    private String rewardPaymentApiUrl;

    @Value( "${reward.payment.api.port}" )
    private int rewardPaymentApiPort;

    @Bean
    public RestTemplate rewardPaymentRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RewardPaymentClient rewardPaymentClient(RestTemplate rewardPaymentRestTemplate) {
        String baseUrl = String.format("%s:%d", rewardPaymentApiUrl, rewardPaymentApiPort);
        return new RewardPaymentClient(baseUrl, rewardPaymentRestTemplate);
    }

}
