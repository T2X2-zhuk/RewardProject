package rewardPayment.rest.cleandb;

import org.slf4j.MDC;
import reactor.core.publisher.Mono;
import rewardPayment.services.CleanDbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/paymentDb")
@RequiredArgsConstructor
@Slf4j
public class CleanPaymentDbController {

    private final CleanDbService cleanDbService;

    @PostMapping(path = "/clean", consumes = "application/json", produces = "application/json")
    public Mono<CleanPaymentDbResponse> cleanDb(@RequestBody CleanPaymentDbRequest request) {

        return Mono.deferContextual(ctx -> {
            String traceId = ctx.getOrDefault("traceId", "no-traceId");
            log.info("[{}] {} is start!", traceId, this.getClass().getSimpleName());

            return cleanDbService.execute(request)
                    .doOnSuccess(r -> log.info("[{}] {} is execute!", traceId, this.getClass().getSimpleName()));
        });
    }
}
