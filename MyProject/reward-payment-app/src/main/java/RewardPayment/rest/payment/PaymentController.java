package RewardPayment.rest.payment;
import RewardPayment.dto.PaymentDTO;
import RewardPayment.requests.CommonRequestForPaymentParameters;
import RewardPayment.responses.CommonResponseForPaymentParameters;
import RewardPayment.services.RewardPaymentService;
import RewardPayment.services.SearchPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping("/reward/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

     private final SearchPaymentService service;
     private final RewardPaymentService rewardPaymentService;

    @GetMapping(path = "/searchPayment",
            produces = "application/json")
    public CommonResponseForPaymentParameters searchPayment(@RequestParam Long employeeId, @RequestParam BigDecimal amount) {
        log.info("{} is start!",this.getClass().getSimpleName());
        CommonRequestForPaymentParameters request = CommonRequestForPaymentParameters.builder()
                .paymentDTO(PaymentDTO.builder()
                        .employeeId(employeeId).amount(amount.setScale(2, RoundingMode.HALF_UP)).build()).build();
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
        log.info("PaymentController is execute!");
        return response;
    }
}
