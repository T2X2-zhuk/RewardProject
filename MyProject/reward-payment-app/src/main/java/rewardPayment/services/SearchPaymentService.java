package rewardPayment.services;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rewardPayment.JPA.domain.Payment;
import rewardPayment.configCache.GetAllPaymentsUsingCache;
import rewardPayment.dto.PaymentDTO;
import rewardPayment.requests.CommonRequestForPaymentParameters;
import rewardPayment.responses.CommonResponseForPaymentParameters;
import rewardPayment.validations.validators.GetPaymentValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@RequiredArgsConstructor
@Slf4j
public class SearchPaymentService {

    private final GetAllPaymentsUsingCache getAllPaymentsUsingCache;
    private final GetPaymentValidator validator;

    public Mono<CommonResponseForPaymentParameters> execute(CommonRequestForPaymentParameters request) {
        log.info("{} is start!", this.getClass().getSimpleName());

        CommonResponseForPaymentParameters response = new CommonResponseForPaymentParameters();

        return validator.validate(request)
                .flatMap(errors -> {
                    log.debug("Validation is execute!");

                    if (!errors.isEmpty()) {
                        log.warn("Validation failed errors : {}", errors);
                        response.setErrors(errors);
                        return Mono.just(response);
                    } else {
                        return buildResponseWithoutErrors(response, request);
                    }
                })
                .doOnSuccess(r -> log.info("{} is execute!", this.getClass().getSimpleName()));
    }

    private Mono<CommonResponseForPaymentParameters> buildResponseWithoutErrors(CommonResponseForPaymentParameters response,
                                                                                CommonRequestForPaymentParameters request) {
        return getPaymentsByEmployeeAndAmount(request.getPaymentDTO().getEmployeeId())
                .map(payment -> PaymentDTO.builder()
                        .id(payment.getId())
                        .employeeId(payment.getEmployeeId())
                        .rewardId(payment.getRewardId())
                        .amount(payment.getAmount())
                        .build())
                .collectList()
                .map(paymentDTOS -> {
                    response.setPaymentDTOS(paymentDTOS);
                    log.info("Payment : {} was successful received", paymentDTOS);
                    return response;
                });
    }

    private Flux<Payment> getPaymentsByEmployeeAndAmount(Long employeeId) {
        // Используем реактивный поток из кеша
        return getAllPaymentsUsingCache.getAllPaymentsWithCache()
                .filter(p -> p.getEmployeeId().equals(employeeId));
    }
}
