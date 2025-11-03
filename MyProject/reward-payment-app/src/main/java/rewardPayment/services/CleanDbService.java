package rewardPayment.services;

import rewardPayment.JPA.repositories.PaymentRepository;
import rewardPayment.configCache.GetAllPaymentsUsingCache;
import rewardPayment.rest.cleandb.CleanPaymentDbRequest;
import rewardPayment.rest.cleandb.CleanPaymentDbResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CleanDbService {

    private final PaymentRepository paymentRepository;
    private final GetAllPaymentsUsingCache getAllPaymentsUsingCache;

    @Transactional
    public CleanPaymentDbResponse execute(CleanPaymentDbRequest request) {
        log.info("{} is start!",this.getClass().getSimpleName());

        CleanPaymentDbResponse response = new CleanPaymentDbResponse();

        if (request.isCleanPayment()) {

            paymentRepository.deleteAll();

            response.setPaymentDeleted(true);

            getAllPaymentsUsingCache.clearPAYMENTSCache();
            log.debug("Payment database is clear!");
        }

        log.info("{} is execute!",this.getClass().getSimpleName());
        return response;
    }
}
