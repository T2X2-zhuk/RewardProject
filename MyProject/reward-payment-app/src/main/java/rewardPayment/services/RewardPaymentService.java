package rewardPayment.services;

import rewardPayment.JPA.domain.Payment;
import rewardPayment.JPA.repositories.PaymentRepository;
import rewardPayment.dto.PaymentDTO;
import rewardPayment.requests.CommonRequestForPaymentParameters;
import rewardPayment.responses.CommonResponseForPaymentParameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RewardPaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public CommonResponseForPaymentParameters pay(CommonRequestForPaymentParameters request) {
        log.info("{} is start!",this.getClass().getSimpleName());

        CommonResponseForPaymentParameters response = new CommonResponseForPaymentParameters();

        List<Payment> payments = new ArrayList<>();

        for (PaymentDTO paymentDTO : request.getPaymentDTOS()){
            log.debug("Save payment with - {} parameters", paymentDTO);

            payments.add(Payment.builder().employeeId(paymentDTO.getEmployeeId()).rewardId(paymentDTO.getRewardId())
                    .amount(paymentDTO.getAmount()).build());
        }

        paymentRepository.saveAll(payments);

        log.debug("Successful saving all payments!");

        response.setSuccessfulSaving(true);

        log.info("{} is execute!",this.getClass().getSimpleName());
        return response;
    }
}
