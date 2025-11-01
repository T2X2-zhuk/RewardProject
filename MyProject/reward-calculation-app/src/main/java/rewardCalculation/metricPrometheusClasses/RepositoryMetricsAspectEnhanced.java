package rewardCalculation.metricPrometheusClasses;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
//Автоматически перехватывает все методы JPA репозиториев.
//Классифицирует операцию как READ/WRITE/OTHER по названию метода.
//Собирает метрики с понятными тегами:
//traceId – для связи с логами.
//repository – имя репозитория.
//method – метод репозитория.
//operation – READ или WRITE.
// status – SUCCESS или FAIL.
//Ведет таймер на выполнение каждого метода.
@Aspect
@Component
@RequiredArgsConstructor
public class RepositoryMetricsAspectEnhanced {

    private final MeterRegistry meterRegistry;

    @Around("execution(* org.springframework.data.repository.CrudRepository+.*(..))")
    public Object recordRepositoryMetrics(ProceedingJoinPoint joinPoint) throws Throwable {

        Timer.Sample sample = Timer.start(meterRegistry);
        String traceId = MDC.get("traceId");
        String status = "SUCCESS";

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        String repositoryName = joinPoint.getTarget().getClass().getSimpleName();

        // Определяем тип операции (READ / WRITE)
        String operationType = classifyOperation(methodName);

        try {
            return joinPoint.proceed();
        } catch (Exception ex) {
            status = "FAIL";
            throw ex;
        } finally {
            // Счётчик
            meterRegistry.counter(
                    "reward.calculation.repository.calls",
                    "traceId", traceId == null ? "none" : traceId,
                    "repository", repositoryName,
                    "method", methodName,
                    "operation", operationType,
                    "status", status
            ).increment();

            // Таймер
            sample.stop(Timer.builder("reward.calculation.repository.duration")
                    .tags(
                            "traceId", traceId == null ? "none" : traceId,
                            "repository", repositoryName,
                            "method", methodName,
                            "operation", operationType,
                            "status", status)
                    .register(meterRegistry));
        }
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
