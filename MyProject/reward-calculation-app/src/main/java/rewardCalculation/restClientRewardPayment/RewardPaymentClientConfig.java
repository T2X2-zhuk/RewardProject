package rewardCalculation.restClientRewardPayment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@Slf4j
public class RewardPaymentClientConfig {

    @Value( "${reward.payment.api.url}" )
    private String rewardPaymentApiUrl;

    @Value( "${reward.payment.api.port}" )
    private int rewardPaymentApiPort;

    @Bean
    public RestTemplate rewardPaymentRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout((int) Duration.ofSeconds(5).toMillis());
        factory.setReadTimeout((int) Duration.ofSeconds(10).toMillis());
        return new RestTemplate(factory);
    }

    @Bean
    public RewardPaymentClient rewardPaymentClient(RestTemplate rewardPaymentRestTemplate) {
        String baseUrl = String.format("%s:%d", rewardPaymentApiUrl, rewardPaymentApiPort);
        log.info("Initialized RewardPaymentClient with baseUrl={}", baseUrl);
        return new RewardPaymentClient(baseUrl, rewardPaymentRestTemplate);
    }
}
