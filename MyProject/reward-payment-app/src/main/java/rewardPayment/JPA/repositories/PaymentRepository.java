package rewardPayment.JPA.repositories;

import rewardPayment.JPA.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
public interface PaymentRepository  extends JpaRepository<Payment, Long> {

}
