package rewardCalculation.transactionalOutBox.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@Slf4j
@ConditionalOnProperty(
        prefix = "app",
        name = ("useAsyncOutboxDispatcher"),
        havingValue = "true"
)
public class AsyncConfig {

    @Value("${app.batch_size}")
    private int batchSize;

    @Bean(name = "outboxTaskExecutor")
    public Executor outboxTaskExecutor() {
        int availableCores = Runtime.getRuntime().availableProcessors();

        // CorePoolSize = min(batchSize, cores * 1.5)
        int corePoolSize = Math.min(batchSize, Math.max(1, (int)(availableCores * 1.5)));

        // MaxPoolSize = batchSize
        int maxPoolSize = batchSize;

        // QueueCapacity = batchSize * 2
        int queueCapacity = batchSize * 2;

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("OutboxAsync-");
        executor.initialize();

        log.info("OutboxTaskExecutor configured: core={}, max={}, queue={}", corePoolSize, maxPoolSize, queueCapacity);

        return executor;
    }
}
