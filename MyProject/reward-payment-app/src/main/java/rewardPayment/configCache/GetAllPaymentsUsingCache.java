package rewardPayment.configCache;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveValueOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rewardPayment.JPA.domain.Payment;
import rewardPayment.JPA.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetAllPaymentsUsingCache {

    private final PaymentRepository repository;
    private final ReactiveRedisOperations<String, Object> redisOperations;

    private static final String CACHE_KEY = "payments";

    public Flux<Payment> getAllPaymentsWithCache() {
        var valueOps = redisOperations.opsForValue();

        return valueOps.get(CACHE_KEY)
                .flatMapMany(cached -> {
                    if (cached instanceof List<?> list && !list.isEmpty() && list.get(0) instanceof Payment p) {
                        log.debug("Payments retrieved from cache");
                        @SuppressWarnings("unchecked")
                        var payments = (List<Payment>) list;
                        return Flux.fromIterable(payments);
                    }
                    return Flux.empty();
                })
                .switchIfEmpty(
                        repository.findAll()
                                .collectList()
                                .flatMapMany(payments ->
                                        valueOps.set(CACHE_KEY, payments, Duration.ofHours(1))
                                                .thenMany(Flux.fromIterable(payments))
                                )
                );
    }

    public Mono<Void> clearPaymentsCache() {
        return redisOperations.delete(CACHE_KEY)
                .doOnSuccess(v -> log.debug("Payments cache cleared"))
                .then();
    }
}