package rewardPayment.JPA.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import rewardPayment.JPA.domain.Payment;

@Repository
public interface PaymentRepository  extends ReactiveCrudRepository<Payment, Long> {

}
