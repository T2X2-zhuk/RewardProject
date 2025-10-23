package rewardCalculation.lock.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();
        // ⚠️ redis-cache — это имя контейнера из docker-compose.yml
        config.useSingleServer()
                .setAddress("redis://redis-cache:6379")
                .setConnectionMinimumIdleSize(1)
                .setConnectionPoolSize(5);
        return Redisson.create(config);
    }
}
