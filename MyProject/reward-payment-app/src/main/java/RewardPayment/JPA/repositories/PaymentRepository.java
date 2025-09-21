package RewardPayment.JPA.repositories;

import RewardPayment.JPA.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository  extends JpaRepository<Payment, Long> {



    Optional<Payment> findByEmployeeIdAndAmount(Long employeeId, Double amount);
}
