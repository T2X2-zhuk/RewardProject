package RewardPayment.services;

import RewardPayment.JPA.domain.Payment;
import RewardPayment.JPA.repositories.PaymentRepository;
import RewardPayment.dto.PaymentDTO;
import RewardPayment.requests.CommonRequestForPaymentParameters;
import RewardPayment.responses.CommonResponseForPaymentParameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RewardPaymentService {

    private final PaymentRepository paymentRepository;

    public CommonResponseForPaymentParameters pay(CommonRequestForPaymentParameters request) {
        log.info("{} is start!",this.getClass().getSimpleName());
        CommonResponseForPaymentParameters response = new CommonResponseForPaymentParameters();
        for (PaymentDTO paymentDTO : request.getPaymentDTOS()){
            log.debug("Save payment with - {} parameters", paymentDTO);
            savePayment(paymentDTO);
        }
        log.debug("Successful saving all payments!");
        response.setSuccessfulSaving(true);
        log.info("{} is execute!",this.getClass().getSimpleName());
        return response;
    }

    private void savePayment(PaymentDTO paymentDTO){
        paymentRepository.save(Payment.builder()
                .employeeId(paymentDTO.getEmployeeId())
                .amount(paymentDTO.getAmount()).build());
    }
}
