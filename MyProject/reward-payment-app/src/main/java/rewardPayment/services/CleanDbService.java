package rewardPayment.services;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import rewardPayment.JPA.repositories.PaymentRepository;
import rewardPayment.configCache.GetAllPaymentsUsingCache;
import rewardPayment.rest.cleandb.CleanPaymentDbRequest;
import rewardPayment.rest.cleandb.CleanPaymentDbResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Service
@RequiredArgsConstructor
@Slf4j
public class CleanDbService {

    private final PaymentRepository paymentRepository;
    private final GetAllPaymentsUsingCache getAllPaymentsUsingCache;

    public Mono<CleanPaymentDbResponse> execute(CleanPaymentDbRequest request) {
        log.info("{} is start!", this.getClass().getSimpleName());

        if (!request.isCleanPayment()) {
            return Mono.just(new CleanPaymentDbResponse());
        }

        return paymentRepository.deleteAll() // реактивное удаление
                .then(getAllPaymentsUsingCache.clearPaymentsCache())
                .then(Mono.fromSupplier(() -> {
                    CleanPaymentDbResponse response = new CleanPaymentDbResponse();
                    response.setPaymentDeleted(true);
                    log.debug("Payment database is cleared!");
                    log.info("{} is execute!", this.getClass().getSimpleName());
                    return response;
                }));
    }
}
