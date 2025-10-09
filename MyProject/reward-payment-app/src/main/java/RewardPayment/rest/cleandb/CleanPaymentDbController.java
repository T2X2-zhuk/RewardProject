package RewardPayment.rest.cleandb;

import RewardPayment.JPA.repositories.PaymentRepository;
import RewardPayment.configCache.GetAllPaymentsUsingCache;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/paymentDb")
@RequiredArgsConstructor
@Slf4j
public class CleanPaymentDbController {

    private final PaymentRepository paymentRepository;
    private final GetAllPaymentsUsingCache getAllPaymentsUsingCache;
    @PostMapping(path = "/clean",
            consumes = "application/json",
            produces = "application/json")
    public CleanPaymentDbResponse cleanDb(@RequestBody CleanPaymentDbRequest request) {
        log.info("{} is start!",this.getClass().getSimpleName());
        CleanPaymentDbResponse response = new CleanPaymentDbResponse();
        if (request.isCleanPayment()) {
            paymentRepository.deleteAll();
            response.setPaymentDeleted(true);
            getAllPaymentsUsingCache.clearPAYMENTSCache();
        }
        log.info("{} is execute!",this.getClass().getSimpleName());
        return response;
    }

}
