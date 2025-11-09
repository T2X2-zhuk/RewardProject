package rewardPayment.rest.payment;

import reactor.core.publisher.Mono;
import rewardPayment.configCache.GetAllPaymentsUsingCache;
import rewardPayment.dto.PaymentDTO;
import rewardPayment.requests.PaymentBatchMessage;
import rewardPayment.requests.CommonRequestForPaymentParameters;
import rewardPayment.responses.CommonResponseForPaymentParameters;
import rewardPayment.services.RewardPaymentService;
import rewardPayment.services.SearchPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reward/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

     private final SearchPaymentService service;
     private final RewardPaymentService rewardPaymentService;
     private final GetAllPaymentsUsingCache getAllPaymentsUsingCache;

    @GetMapping(path = "/searchPayment/{employeeId}", produces = "application/json")
    public Mono<CommonResponseForPaymentParameters> searchPaymentByEmployeeId(@PathVariable Long employeeId) {
        return Mono.deferContextual(ctx -> {
            String traceId = ctx.getOrDefault("traceId", "no-traceId");

            log.info("[{}] {} is start!", traceId, this.getClass().getSimpleName());

            CommonRequestForPaymentParameters request = CommonRequestForPaymentParameters.builder()
                    .paymentDTO(PaymentDTO.builder().employeeId(employeeId).build())
                    .build();

            return service.execute(request)
                    .doOnSuccess(r -> log.info("[{}] {} is execute!", traceId, this.getClass().getSimpleName()));
        });
    }

    @PostMapping(path = "/payReward", consumes = "application/json", produces = "application/json")
    public Mono<CommonResponseForPaymentParameters> payReward(@RequestBody CommonRequestForPaymentParameters request) {
        return Mono.deferContextual(ctx -> {
            String traceId = ctx.getOrDefault("traceId", "no-traceId");

            log.info("[{}] {} is start!", traceId, this.getClass().getSimpleName());

            return rewardPaymentService.pay(new PaymentBatchMessage(request.getPaymentDTOS()))
                    .flatMap(r -> getAllPaymentsUsingCache.clearPaymentsCache().thenReturn(r))
                    .doOnSuccess(r -> log.info("[{}] {} is execute!", traceId, this.getClass().getSimpleName()));
        });
    }
}
