package RewardPayment.rest;
import RewardPayment.responses.GetPaymentByEmployeeIdAndAmountResponse;
import RewardPayment.services.SearchPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/payment")
public class SearchPaymentController {

    @Autowired private SearchPaymentService service;

    @GetMapping(path = "/search",
            produces = "application/json")
    public GetPaymentByEmployeeIdAndAmountResponse searchPayment(@RequestParam Long employeeId, @RequestParam Double amount) {
        return service.execute(employeeId,amount);
    }
}
