package RewardPayment.services;

import RewardPayment.JPA.domain.Payment;
import RewardPayment.JPA.repositories.PaymentRepository;
import RewardPayment.dto.PaymentDTO;
import RewardPayment.requests.CommonRequestForPaymentParameters;
import RewardPayment.responses.CommonResponseForPaymentParameters;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
@RequiredArgsConstructor
@ToString
@Slf4j
public class RewardPaymentService {

    private final PaymentRepository paymentRepository;

    public CommonResponseForPaymentParameters pay(CommonRequestForPaymentParameters request) {
        log.info("{} is start!", this);
        CommonResponseForPaymentParameters response = new CommonResponseForPaymentParameters();
        List<PaymentDTO> paymentDTOs = new ArrayList<>();
        for (PaymentDTO paymentDTO : request.getPaymentDTOS()){
            log.debug("Save payment with - {} parameters", paymentDTO);
            savePayment(paymentDTO);
            log.debug("Add save payment in paymentDTO list");
            addSavePaymentInPaymentDTOList(paymentDTOs, paymentDTO);
        }
        response.setStatus("PAID");
        response.setPaymentDTOS(paymentDTOs);
        log.info("{} is execute!", this);
        return response;
    }

    private void addSavePaymentInPaymentDTOList(List<PaymentDTO> paymentDTOs, PaymentDTO paymentDTO) {
        Optional<Payment> payment = paymentRepository.findByEmployeeIdAndAmount(paymentDTO.getEmployeeId(), paymentDTO.getAmount());
        paymentDTOs.add(PaymentDTO.builder()
                .id(payment.get().getId())
                .employeeId(payment.get().getEmployeeId())
                .amount(payment.get().getAmount()).build());
    }

    private void savePayment(PaymentDTO paymentDTO){
        paymentRepository.save(Payment.builder()
                .employeeId(paymentDTO.getEmployeeId())
                .amount(paymentDTO.getAmount()).build());
    }
}
