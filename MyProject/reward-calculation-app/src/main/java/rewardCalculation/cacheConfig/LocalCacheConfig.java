package rewardCalculation.cacheConfig;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class LocalCacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("tariffs");
        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .maximumSize(1000)            // максимальное количество элементов
                        .expireAfterWrite(1, TimeUnit.HOURS) // TTL 1 час
        );
        return cacheManager;
    }

}
