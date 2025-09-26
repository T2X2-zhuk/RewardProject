package RewardPayment.rest.payment;
import RewardPayment.dto.PaymentDTO;
import RewardPayment.requests.CommonRequestForPaymentParameters;
import RewardPayment.responses.CommonResponseForPaymentParameters;
import RewardPayment.services.RewardPaymentService;
import RewardPayment.services.SearchPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reward/payment")
public class PaymentController {

    @Autowired private SearchPaymentService service;
    @Autowired private RewardPaymentService rewardPaymentService;

    @GetMapping(path = "/searchPayment",
            produces = "application/json")
    public CommonResponseForPaymentParameters searchPayment(@RequestParam Long employeeId, @RequestParam Double amount) {
        CommonRequestForPaymentParameters request = CommonRequestForPaymentParameters.builder()
                .paymentDTO(PaymentDTO.builder()
                        .employeeId(employeeId).amount(amount).build()).build();
        return service.execute(request);
    }

    @PostMapping(path = "/payReward",
            consumes = "application/json",
            produces = "application/json")
    public CommonResponseForPaymentParameters payReward(@RequestBody CommonRequestForPaymentParameters request) {
        return rewardPaymentService.pay(request);
    }
}
