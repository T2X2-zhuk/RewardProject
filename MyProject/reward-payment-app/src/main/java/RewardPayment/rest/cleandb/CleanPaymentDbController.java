package RewardPayment.rest.cleandb;

import RewardPayment.JPA.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/paymentDb")
public class CleanPaymentDbController {

    @Autowired private PaymentRepository paymentRepository;

    @PostMapping(path = "/clean",
            consumes = "application/json",
            produces = "application/json")
    public CleanPaymentDbResponse cleanDb(@RequestBody CleanPaymentDbRequest request) {
        CleanPaymentDbResponse response = new CleanPaymentDbResponse();
        if (request.isCleanPayment()) {
            paymentRepository.deleteAll();
            response.setPaymentDeleted(true);
        }
        return response;
    }

}
