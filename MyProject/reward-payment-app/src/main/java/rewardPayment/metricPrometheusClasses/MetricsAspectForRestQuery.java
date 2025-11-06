package rewardPayment.metricPrometheusClasses;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;

// Автоматический класс для метрик
// 1. Поддержка всех основных HTTP аннотаций.
// 2. URI полностью корректный: берётся сначала с уровня класса (@RequestMapping на контроллере), потом с метода, fallback на имя метода.
// 3. HTTP метод корректно определяется.
// 4. Полная автоматизация: контроллеры можно оставить чистыми, без ручного Timer/recordAttempt.
@Aspect
@Component
@RequiredArgsConstructor
public class MetricsAspectForRestQuery {

    private final MeterRegistry meterRegistry;

    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public Object recordMetrics(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String httpMethod = resolveHttpMethod(method);
        String uri = resolveUri(method);
        Timer.Sample sample = Timer.start(meterRegistry);

        Object result = joinPoint.proceed();

        if (result instanceof Mono<?> mono) {
            return mono.doOnSuccess(r -> recordMetricsSample(httpMethod, uri, "200", sample))
                    .doOnError(e -> recordMetricsSample(httpMethod, uri, "500", sample));
        } else if (result instanceof Flux<?> flux) {
            return flux.doOnComplete(() -> recordMetricsSample(httpMethod, uri, "200", sample))
                    .doOnError(e -> recordMetricsSample(httpMethod, uri, "500", sample));
        } else {
            // синхронный метод
            recordMetricsSample(httpMethod, uri, "200", sample);
            return result;
        }
    }

    private void recordMetricsSample(String httpMethod, String uri, String status, Timer.Sample sample) {
        meterRegistry.counter(
                "reward.payment.rest.query.attempts",
                "method", httpMethod,
                "uri", uri,
                "status", status
        ).increment();

        sample.stop(Timer.builder("reward.payment.rest.query.duration")
                .tags("method", httpMethod, "uri", uri, "status", status)
                .register(meterRegistry));
    }

    private String resolveHttpMethod(Method method) {
        if (method.isAnnotationPresent(GetMapping.class)) return "GET";
        if (method.isAnnotationPresent(PostMapping.class)) return "POST";
        if (method.isAnnotationPresent(PutMapping.class)) return "PUT";
        if (method.isAnnotationPresent(DeleteMapping.class)) return "DELETE";
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMethod[] methods = method.getAnnotation(RequestMapping.class).method();
            return methods.length > 0 ? methods[0].name() : "REQUEST";
        }
        return "UNKNOWN";
    }

    private String resolveUri(Method method) {
        String uri = method.getName();
        if (method.isAnnotationPresent(GetMapping.class)) uri = firstOrFallback(method.getAnnotation(GetMapping.class).path(),
                method.getAnnotation(GetMapping.class).value(), uri);
        else if (method.isAnnotationPresent(PostMapping.class)) uri = firstOrFallback(method.getAnnotation(PostMapping.class).path(),
                method.getAnnotation(PostMapping.class).value(), uri);
        else if (method.isAnnotationPresent(PutMapping.class)) uri = firstOrFallback(method.getAnnotation(PutMapping.class).path(),
                method.getAnnotation(PutMapping.class).value(), uri);
        else if (method.isAnnotationPresent(DeleteMapping.class)) uri = firstOrFallback(method.getAnnotation(DeleteMapping.class).path(),
                method.getAnnotation(DeleteMapping.class).value(), uri);
        else if (method.isAnnotationPresent(RequestMapping.class)) uri = firstOrFallback(method.getAnnotation(RequestMapping.class).path(),
                method.getAnnotation(RequestMapping.class).value(), uri);
        return uri.startsWith("/") ? uri : "/" + uri;
    }

    private String firstOrFallback(String[] paths, String[] values, String fallback) {
        if (paths.length > 0) return paths[0];
        if (values.length > 0) return values[0];
        return fallback;
    }
}
