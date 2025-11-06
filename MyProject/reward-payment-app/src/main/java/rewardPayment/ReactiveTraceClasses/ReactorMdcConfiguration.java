package rewardPayment.ReactiveTraceClasses;


import jakarta.annotation.PostConstruct;
import org.reactivestreams.Subscription;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Operators;
import reactor.util.context.Context;

@Component
public class ReactorMdcConfiguration {

    @PostConstruct
    public void setupHooks() {
        // Регистрируем глобальный хук (уникальный ID, чтобы не дублировался)
        Hooks.onEachOperator("reactor.mdc", Operators.lift((scannable, subscriber) ->
                new CoreSubscriber<>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscriber.onSubscribe(s);
                    }

                    @Override
                    public void onNext(Object value) {
                        copyContextToMdc(subscriber.currentContext());
                        subscriber.onNext(value);
                    }

                    @Override
                    public void onError(Throwable t) {
                        copyContextToMdc(subscriber.currentContext());
                        subscriber.onError(t);
                    }

                    @Override
                    public void onComplete() {
                        copyContextToMdc(subscriber.currentContext());
                        subscriber.onComplete();
                    }

                    @Override
                    public Context currentContext() {
                        return subscriber.currentContext();
                    }
                }
        ));
    }

    private void copyContextToMdc(Context context) {
        if (context.hasKey(TraceIdFilter.TRACE_ID)) {
            MDC.put(TraceIdFilter.TRACE_ID, context.get(TraceIdFilter.TRACE_ID));
        } else {
            MDC.remove(TraceIdFilter.TRACE_ID);
        }
    }
}
