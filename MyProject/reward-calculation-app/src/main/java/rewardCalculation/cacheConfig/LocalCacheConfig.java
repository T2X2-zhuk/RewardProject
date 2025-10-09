package rewardCalculation.cacheConfig;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class LocalCacheConfig {

    public static final String TARIFF_CACHE = "tariffs";
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(TARIFF_CACHE);
        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .maximumSize(10)            // максимальное количество элементов
                        .expireAfterWrite(1, TimeUnit.HOURS) // TTL 1 час
        );
        return cacheManager;
    }

}
