package RewardPayment.util.forServices;

import RewardPayment.JPA.domain.Payment;
import RewardPayment.JPA.repositories.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionalClassWithMethodsForPayment {

    private final PaymentRepository paymentRepository;

    @Transactional
    public void savePayment(Payment payment){
        log.info("Save payment - {}, {}" , payment.getEmployeeId() , payment.getAmount());
      paymentRepository.save(payment);
    }
    @Transactional
    public void saveAllPayment(List<Payment> payments){
        log.info("Save all payments - {}",payments);
        paymentRepository.saveAll(payments);
    }
    @Transactional
    public void deleteAllPayments(){
        log.info("Delete all payments");
        paymentRepository.deleteAll();
        log.debug("Delete is successful!");
    }
}
