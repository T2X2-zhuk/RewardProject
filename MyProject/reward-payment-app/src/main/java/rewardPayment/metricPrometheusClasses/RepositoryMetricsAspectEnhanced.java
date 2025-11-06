package rewardPayment.metricPrometheusClasses;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//Автоматически перехватывает все методы JPA репозиториев.
//Собирает метрики
//Ведет таймер на выполнение каждого метода.
@Aspect
@Component
@RequiredArgsConstructor
public class RepositoryMetricsAspectEnhanced {

    private final MeterRegistry meterRegistry;

    @Around("execution(* org.springframework.data.repository.reactive.ReactiveCrudRepository+.*(..))")
    public Object recordRepositoryMetrics(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        String repositoryName = joinPoint.getTarget().getClass().getSimpleName();
        String operationType = classifyOperation(methodName);

        try {
            Object result = joinPoint.proceed();
            if (result instanceof Mono<?> mono) {
                Timer.Sample sample = Timer.start(meterRegistry);
                return mono
                        .doOnSuccess(r -> meterRegistration(repositoryName, methodName, operationType, "SUCCESS", sample))
                        .doOnError(e -> meterRegistration(repositoryName, methodName, operationType, "FAIL", sample));
            } else if (result instanceof Flux<?> flux) {
                Timer.Sample sample = Timer.start(meterRegistry);
                return flux
                        .doOnComplete(() -> meterRegistration(repositoryName, methodName, operationType, "SUCCESS", sample))
                        .doOnError(e -> meterRegistration(repositoryName, methodName, operationType, "FAIL", sample));
            } else {
                Timer.Sample sample = Timer.start(meterRegistry);
                meterRegistration(repositoryName, methodName, operationType, "SUCCESS", sample);
                return result;
            }
        } catch (Exception ex) {
            throw ex;
        }
    }
    private void meterRegistration(String repositoryName, String methodName, String operationType, String status, Timer.Sample sample) {
        // Счётчик
        meterRegistry.counter(
                "reward.payment.repository.calls",
                "repository", repositoryName,
                "method", methodName,
                "operation", operationType,
                "status", status
        ).increment();

        // Таймер
        sample.stop(Timer.builder("reward.payment.repository.duration")
                .tags(
                        "repository", repositoryName,
                        "method", methodName,
                        "operation", operationType,
                        "status", status)
                .register(meterRegistry));
    }

    private String classifyOperation(String methodName) {
        String lower = methodName.toLowerCase();
        if (lower.startsWith("find") || lower.startsWith("get") || lower.startsWith("read") || lower.startsWith("count") || lower.startsWith("exists")) {
            return "READ";
        } else if (lower.startsWith("save") || lower.startsWith("update") || lower.startsWith("delete") || lower.startsWith("remove")) {
            return "WRITE";
        } else {
            return "OTHER";
        }
    }
}
