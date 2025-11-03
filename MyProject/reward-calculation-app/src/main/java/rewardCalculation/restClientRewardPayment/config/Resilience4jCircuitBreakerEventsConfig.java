package rewardCalculation.restClientRewardPayment.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnErrorEvent;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnSuccessEvent;
import io.github.resilience4j.micrometer.tagged.TaggedCircuitBreakerMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
@Slf4j
public class Resilience4jCircuitBreakerEventsConfig {

    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final MeterRegistry meterRegistry;

    @PostConstruct
    public void init() {
        // 1️⃣ Привязка CircuitBreaker к Micrometer/Prometheus
        TaggedCircuitBreakerMetrics.ofCircuitBreakerRegistry(circuitBreakerRegistry)
                .bindTo(meterRegistry);
        log.info("✅ CircuitBreaker metrics bound to Micrometer/Prometheus");

        // 2️⃣ Подписка на события всех CircuitBreaker
        circuitBreakerRegistry.getAllCircuitBreakers()
                .forEach(this::execute);

        // 3️⃣ Подписка на новые CircuitBreaker, которые могут быть добавлены позже
        circuitBreakerRegistry.getEventPublisher()
                .onEntryAdded(event -> {
                    CircuitBreaker addedCb = event.getAddedEntry();
                    log.info("CircuitBreaker added to registry: {}", addedCb.getName());
                    execute(addedCb);
                });

        log.info("Resilience4j CircuitBreaker event listeners registered.");
    }

    private void execute(CircuitBreaker cb) {
        String name = cb.getName();

        cb.getEventPublisher()
                .onStateTransition(evt -> log.warn("[CB:{}] StateTransition: {} -> {} (time={})",
                        name,
                        evt.getStateTransition().getFromState(),
                        evt.getStateTransition().getToState(),
                        evt.getCreationTime()))
                .onError(evt -> log.error("[CB:{}] Call failed: type={}, exception={}, durationMs={}",
                        name,
                        evt.getEventType(),
                        evt.getThrowable() != null ? evt.getThrowable() : "null",
                        evt.getElapsedDuration().toMillis()))
                .onSuccess(evt -> log.info("[CB:{}] Call success: durationMs={}",
                        name,
                        evt.getElapsedDuration().toMillis()))
                .onCallNotPermitted(evt -> log.warn("[CB:{}] Call not permitted (OPEN). Time={}",
                        name,
                        evt.getCreationTime()));
    }

}
