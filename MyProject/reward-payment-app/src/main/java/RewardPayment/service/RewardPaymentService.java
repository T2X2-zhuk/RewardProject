package RewardPayment.service;

import RewardPayment.JPA.domain.Payment;
import RewardPayment.JPA.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class RewardPaymentService {

    @Autowired private PaymentRepository paymentRepository;

    public void pay(Long employeeId, Double amount) {
        Payment payment = new Payment();
        payment.setEmployeeId(employeeId);
        payment.setAmount(amount);
        paymentRepository.save(payment);
    }

}
