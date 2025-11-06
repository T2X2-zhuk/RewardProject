package rewardPayment.ReactiveTraceClasses;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Component
public class TraceIdFilter implements WebFilter {

    public static final String TRACE_ID = "traceId";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        //Всегда создаём новый traceId (игнорируем входящий заголовок)
        String traceId = UUID.randomUUID().toString();

        //Добавляем traceId в ответ клиенту
        exchange.getResponse().getHeaders().set("X-Trace-Id", traceId);

        //Передаём traceId в Reactor Context, чтобы был доступен в цепочке
        return chain.filter(exchange)
                .contextWrite(ctx -> ctx.put(TRACE_ID, traceId));
    }
}
