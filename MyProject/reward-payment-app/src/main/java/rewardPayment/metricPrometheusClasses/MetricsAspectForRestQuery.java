package rewardPayment.metricPrometheusClasses;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;

// Автоматический класс для метрик
// 1. Поддержка всех основных HTTP аннотаций.
// 2. URI полностью корректный: берётся сначала с уровня класса (@RequestMapping на контроллере), потом с метода, fallback на имя метода.
// 3. HTTP метод корректно определяется.
// 4. traceId и status сохраняются для мониторинга.
// 5. Полная автоматизация: контроллеры можно оставить чистыми, без ручного Timer/recordAttempt.
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
        String traceId = MDC.get("traceId");
        String status = "200";

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method methodObj = signature.getMethod();

        // HTTP method и URI по умолчанию
        String httpMethod = "UNKNOWN";
        String uri = joinPoint.getSignature().toShortString();

        // Получаем URI с уровня класса (если есть)
        Class<?> controllerClass = methodObj.getDeclaringClass();
        String classPath = "";
        if (controllerClass.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping classRequest = controllerClass.getAnnotation(RequestMapping.class);
            if (classRequest.path().length > 0) classPath = classRequest.path()[0];
            else if (classRequest.value().length > 0) classPath = classRequest.value()[0];
        }

        // Получаем HTTP метод и URI с уровня метода
        if (methodObj.isAnnotationPresent(GetMapping.class)) {
            GetMapping ann = methodObj.getAnnotation(GetMapping.class);
            httpMethod = "GET";
            uri = resolveUri(classPath, ann.path(), ann.value(), uri);
        } else if (methodObj.isAnnotationPresent(PostMapping.class)) {
            PostMapping ann = methodObj.getAnnotation(PostMapping.class);
            httpMethod = "POST";
            uri = resolveUri(classPath, ann.path(), ann.value(), uri);
        } else if (methodObj.isAnnotationPresent(PutMapping.class)) {
            PutMapping ann = methodObj.getAnnotation(PutMapping.class);
            httpMethod = "PUT";
            uri = resolveUri(classPath, ann.path(), ann.value(), uri);
        } else if (methodObj.isAnnotationPresent(DeleteMapping.class)) {
            DeleteMapping ann = methodObj.getAnnotation(DeleteMapping.class);
            httpMethod = "DELETE";
            uri = resolveUri(classPath, ann.path(), ann.value(), uri);
        } else if (methodObj.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping ann = methodObj.getAnnotation(RequestMapping.class);
            RequestMethod[] methods = ann.method();
            httpMethod = methods.length > 0 ? methods[0].name() : "REQUEST";
            uri = resolveUri(classPath, ann.path(), ann.value(), uri);
        }

        try {
            return joinPoint.proceed();
        } catch (Exception ex) {
            status = "500";
            throw ex;
        } finally {
            // Счётчик
            meterRegistry.counter(
                    "reward.payment.rest.query.attempts",
                    "traceId", traceId == null ? "none" : traceId,
                    "method", httpMethod,
                    "uri", uri,
                    "status", status
            ).increment();

            // Таймер
            sample.stop(Timer.builder("reward.payment.rest.query.duration")
                    .tags(
                            "traceId", traceId == null ? "none" : traceId,
                            "method", httpMethod,
                            "uri", uri,
                            "status", status)
                    .register(meterRegistry));
        }
    }

    private String resolveUri(String classPath, String[] pathArray, String[] valueArray, String fallback) {
        String methodPath = fallback;
        if (pathArray.length > 0) methodPath = pathArray[0];
        else if (valueArray.length > 0) methodPath = valueArray[0];

        if (!classPath.isEmpty()) {
            if (!methodPath.startsWith("/")) methodPath = "/" + methodPath;
            methodPath = classPath + methodPath;
        }
        return methodPath;
    }
}
