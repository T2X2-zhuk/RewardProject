package rewardPayment.metricPrometheusClasses;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

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
        Timer.Sample sample = Timer.start(meterRegistry);
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // Определяем HTTP метод и URI
        HttpInfo httpInfo = resolveHttpMethodAndUri(method);

        String traceId = MDC.get("traceId");
        Object result;
        String status = "UNKNOWN";

        try {
            result = joinPoint.proceed();
            status = getResponseStatus();
            return result;
        } catch (Exception ex) {
            status = "500";
            throw ex;
        } finally {
            String bestPattern = getBestMatchingPattern();
            if (bestPattern != null) httpInfo.uri = bestPattern;

            meterRegistration(httpInfo.method, httpInfo.uri, status, sample);

            if (traceId != null) {
                System.out.printf("[traceId=%s] %s %s -> %s%n", traceId, httpInfo.method, httpInfo.uri, status);
            }
        }
    }

    private void meterRegistration(String method, String uri, String status, Timer.Sample sample) {
        meterRegistry.counter(
                "reward.payment.rest.query.attempts",
                "method", method,
                "uri", uri,
                "status", status
        ).increment();

        sample.stop(Timer.builder("reward.payment.rest.query.duration")
                .tags("method", method, "uri", uri, "status", status)
                .register(meterRegistry));
    }

    private HttpInfo resolveHttpMethodAndUri(Method method) {
        String httpMethod = "UNKNOWN";
        String uri = method.getName();

        Class<?> cls = method.getDeclaringClass();
        String classPath = "";
        if (cls.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping classAnn = cls.getAnnotation(RequestMapping.class);
            classPath = firstOrEmpty(classAnn.path(), classAnn.value());
        }

        if (method.isAnnotationPresent(GetMapping.class)) {
            httpMethod = "GET";
            uri = firstOrFallback(method.getAnnotation(GetMapping.class).path(),
                    method.getAnnotation(GetMapping.class).value(), uri);
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            httpMethod = "POST";
            uri = firstOrFallback(method.getAnnotation(PostMapping.class).path(),
                    method.getAnnotation(PostMapping.class).value(), uri);
        } else if (method.isAnnotationPresent(PutMapping.class)) {
            httpMethod = "PUT";
            uri = firstOrFallback(method.getAnnotation(PutMapping.class).path(),
                    method.getAnnotation(PutMapping.class).value(), uri);
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            httpMethod = "DELETE";
            uri = firstOrFallback(method.getAnnotation(DeleteMapping.class).path(),
                    method.getAnnotation(DeleteMapping.class).value(), uri);
        } else if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping ann = method.getAnnotation(RequestMapping.class);
            RequestMethod[] methods = ann.method();
            httpMethod = methods.length > 0 ? methods[0].name() : "REQUEST";
            uri = firstOrFallback(ann.path(), ann.value(), uri);
        }

        if (!classPath.isEmpty() && !uri.startsWith("/")) uri = "/" + uri;
        if (!classPath.isEmpty()) uri = classPath + uri;

        return new HttpInfo(httpMethod, uri);
    }

    private String firstOrFallback(String[] paths, String[] values, String fallback) {
        if (paths.length > 0) return paths[0];
        if (values.length > 0) return values[0];
        return fallback;
    }

    private String firstOrEmpty(String[] paths, String[] values) {
        if (paths.length > 0) return paths[0];
        if (values.length > 0) return values[0];
        return "";
    }

    private String getBestMatchingPattern() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof ServletRequestAttributes sra) {
            Object pattern = sra.getRequest().getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
            if (pattern instanceof String s) return s;
        }
        return null;
    }

    private String getResponseStatus() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof ServletRequestAttributes sra) {
            HttpServletResponse response = sra.getResponse();
            if (response != null) return String.valueOf(response.getStatus());
        }
        return "UNKNOWN";
    }

    private static class HttpInfo {
        String method;
        String uri;
        HttpInfo(String method, String uri) { this.method = method; this.uri = uri; }
    }
}
