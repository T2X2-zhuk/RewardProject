package rewardPayment.configCache;


import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Bean
    public ReactiveRedisOperations<String, Object> reactiveRedisOperations(ReactiveRedisConnectionFactory factory) {
        var keySerializer = new StringRedisSerializer();
        var valueSerializer = new GenericJackson2JsonRedisSerializer();

        var context = RedisSerializationContext
                .<String, Object>newSerializationContext(keySerializer)
                .value(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer))
                .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}
