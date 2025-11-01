package rewardPayment.rest.cleandb;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.MDC;
import rewardPayment.lock.RewardExecutionLock;
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
    private final RewardExecutionLock rewardExecutionLock;

    @PostMapping(path = "/clean",
            consumes = "application/json",
            produces = "application/json")
    public CleanPaymentDbResponse cleanDb(@RequestBody CleanPaymentDbRequest request) {

        return rewardExecutionLock.runWithLock("rewardPayment", () -> {
            log.info("[{}] {} is start!", MDC.get("traceId"), this.getClass().getSimpleName());
            CleanPaymentDbResponse innerResponse = cleanDbService.execute(request);
            log.info("[{}] {} is execute!", MDC.get("traceId"), this.getClass().getSimpleName());
            return innerResponse;
        });
    }
}
