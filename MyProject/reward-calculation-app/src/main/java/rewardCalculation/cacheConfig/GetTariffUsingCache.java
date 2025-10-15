package rewardCalculation.cacheConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import rewardCalculation.JPA.domain.Tariff;
import rewardCalculation.JPA.repositories.TariffRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetTariffUsingCache {

    private final TariffRepository tariffRepository;

    @Cacheable(cacheNames = RedisCacheConfig.TARIFF_CACHE)
    public Map<String, Tariff> getTariffsByJobType() {
        List<Tariff> tariffs = tariffRepository.findAll();
        return tariffs.stream().collect(Collectors.toMap(Tariff::getJobType, t -> t));
    }

    @CacheEvict(value = RedisCacheConfig.TARIFF_CACHE,allEntries = true)
    public void clearTARIFF_CACHECache(){

    }

}
