package RewardPayment.configCache;


import RewardPayment.JPA.domain.Payment;
import RewardPayment.JPA.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllPaymentsUsingCache {

    private final PaymentRepository repository;

    @Cacheable(cacheNames = RedisCacheConfig.PAYMENTS)
    public List<Payment> getAllPaymentsWithCache() {
        return repository.findAll();
    }
    @CacheEvict(value = RedisCacheConfig.PAYMENTS,allEntries = true)
    public void clearPAYMENTSCache(){

    }
}
