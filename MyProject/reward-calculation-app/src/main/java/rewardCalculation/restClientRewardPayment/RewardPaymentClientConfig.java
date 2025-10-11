package rewardCalculation.restClientRewardPayment;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        return new RestTemplate();
    }

    @Bean
    public RewardPaymentClient rewardPaymentClient(RestTemplate rewardPaymentRestTemplate) {
        String baseUrl = String.format("%s:%d", rewardPaymentApiUrl, rewardPaymentApiPort);
        return new RewardPaymentClient(baseUrl, rewardPaymentRestTemplate);
    }

    @Bean
    public Retry rewardPaymentRetry() {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMinutes(1))
                .retryExceptions(Exception.class) // любые исключения для демонстрации
                .build();

        Retry retry = Retry.of("rewardPaymentRetry", config);

        retry.getEventPublisher()
                .onRetry(event -> log.warn("Retry attempt #{} for {} due to {}",
                        event.getNumberOfRetryAttempts(),
                        event.getName(),
                        event.getLastThrowable() != null ? event.getLastThrowable().getMessage() : "none"));

        return retry;
    }

}
