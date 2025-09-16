package RewardPayment.JPA.repositories;

import RewardPayment.JPA.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository  extends JpaRepository<Payment, Long> {


}
