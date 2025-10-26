package rewardPayment.rest.payment;
import rewardPayment.configCache.GetAllPaymentsUsingCache;
import rewardPayment.dto.PaymentDTO;
import rewardPayment.lock.RewardExecutionLock;
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

    @GetMapping(path = "/searchPayment/{employeeId}",
            produces = "application/json")
    public CommonResponseForPaymentParameters searchPaymentByEmployeeId(@PathVariable Long employeeId) {
        log.info("{} is start!",this.getClass().getSimpleName());
        CommonRequestForPaymentParameters request = CommonRequestForPaymentParameters.builder()
                .paymentDTO(PaymentDTO.builder()
                        .employeeId(employeeId).build()).build();
        CommonResponseForPaymentParameters response =  service.execute(request);
        log.info("{} is execute!",this.getClass().getSimpleName());
        return response;
    }

    @PostMapping(path = "/payReward",
            consumes = "application/json",
            produces = "application/json")
    public CommonResponseForPaymentParameters payReward(@RequestBody CommonRequestForPaymentParameters request) {
        log.info("PaymentController is start!");
        CommonResponseForPaymentParameters response = rewardPaymentService.pay(request);
        getAllPaymentsUsingCache.clearPAYMENTSCache();
        log.info("PaymentController is execute!");
        return response;
    }
}
