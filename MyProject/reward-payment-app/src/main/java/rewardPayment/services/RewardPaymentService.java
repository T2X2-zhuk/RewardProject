package rewardPayment.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rewardPayment.JPA.domain.Payment;
import rewardPayment.JPA.repositories.PaymentRepository;
import rewardPayment.requests.CommonRequestForPaymentParameters;
import rewardPayment.responses.CommonResponseForPaymentParameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RewardPaymentService {

    private final PaymentRepository paymentRepository;

    public Mono<CommonResponseForPaymentParameters> pay(CommonRequestForPaymentParameters request) {
        log.info("{} is start!", this.getClass().getSimpleName());

        return paymentRepository.saveAll(convertingDTOSToPayments(request))
                .collectList() // дождаться завершения всех сохранений
                .map(savedPayments -> {
                    log.debug("Successful saving all payments!");
                    CommonResponseForPaymentParameters response = new CommonResponseForPaymentParameters();
                    response.setSuccessfulSaving(true);
                    return response;
                })
                .doOnSuccess(r -> log.info("{} is execute!", this.getClass().getSimpleName()));
    }

    private Flux<Payment> convertingDTOSToPayments(CommonRequestForPaymentParameters request) {
        return Flux.fromIterable(request.getPaymentDTOS())
                .map(paymentDTO -> {
                    log.debug("Save payment with - {} parameters", paymentDTO);
                    return Payment.builder()
                            .employeeId(paymentDTO.getEmployeeId())
                            .rewardId(paymentDTO.getRewardId())
                            .amount(paymentDTO.getAmount())
                            .build();
                });
    }
}
