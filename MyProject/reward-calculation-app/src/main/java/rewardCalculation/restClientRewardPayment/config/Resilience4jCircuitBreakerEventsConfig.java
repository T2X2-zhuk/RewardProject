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
                .forEach(this::registerListenersToSingleCb);

        // 3️⃣ Подписка на новые CircuitBreaker, которые могут быть добавлены позже
        circuitBreakerRegistry.getEventPublisher()
                .onEntryAdded(event -> {
                    CircuitBreaker addedCb = event.getAddedEntry();
                    log.info("CircuitBreaker added to registry: {}", addedCb.getName());
                    registerListenersToSingleCb(addedCb);
                });

        log.info("Resilience4j CircuitBreaker event listeners registered.");
    }

    private void registerListenersToSingleCb(CircuitBreaker cb) {
        String name = cb.getName();

        cb.getEventPublisher()
                // Переход состояния (CLOSED ↔ OPEN ↔ HALF_OPEN)
                .onStateTransition(evt -> {
                    var t = evt.getStateTransition();
                    log.warn("[CB:{}] StateTransition: {} -> {} (time={})",
                            name, t.getFromState(), t.getToState(), evt.getCreationTime());
                })
                // Ошибки вызовов
                .onError(evt -> {
                    log.error("[CB:{}] Call failed: type={}, exception={}, durationMs={}",
                            name,
                            evt.getEventType(),
                            evt.getThrowable() != null ? evt.getThrowable().toString() : "null",
                            evt instanceof CircuitBreakerOnErrorEvent ?
                                    ((CircuitBreakerOnErrorEvent) evt).getElapsedDuration().toMillis() : -1);
                })
                // Успешные вызовы
                .onSuccess(evt -> {
                    if (evt instanceof CircuitBreakerOnSuccessEvent successEvt) {
                        log.info("[CB:{}] Call success: durationMs={}",
                                name, successEvt.getElapsedDuration().toMillis());
                    }
                })
                // Вызовы, запрещённые из-за открытой цепи
                .onCallNotPermitted(evt -> {
                    log.warn("[CB:{}] Call not permitted (OPEN). Time={}", name, evt.getCreationTime());
                });
    }
}
