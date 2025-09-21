package RewardPayment.services;

import RewardPayment.JPA.domain.Payment;
import RewardPayment.JPA.repositories.PaymentRepository;
import RewardPayment.dto.PaymentDTO;
import RewardPayment.restClientRewardCalculation.RewardPaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class RewardPaymentService {

    @Autowired private PaymentRepository paymentRepository;

    public RewardPaymentResponse pay(List<PaymentDTO> paymentDTOs) {
        RewardPaymentResponse response = new RewardPaymentResponse();
        for (PaymentDTO paymentDTO : paymentDTOs){
            Payment payment = Payment.builder()
                    .employeeId(paymentDTO.getEmployeeId())
                    .amount(paymentDTO.getAmount()).build();
            paymentRepository.save(payment);
        }
        response.setStatus("PAID");
        response.setPaymentDTOS(paymentDTOs);
        return response;
    }



}
