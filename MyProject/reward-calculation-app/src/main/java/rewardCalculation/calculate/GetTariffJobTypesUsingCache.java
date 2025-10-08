package rewardCalculation.calculate;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import rewardCalculation.JPA.domain.Tariff;
import rewardCalculation.JPA.repositories.TariffRepository;

import javax.swing.plaf.TabbedPaneUI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetTariffJobTypesUsingCache {

    private final TariffRepository repository;

    @Cacheable("tariffs")
    public Map<String, Tariff> getTariffsByJobType() {
        List<Tariff> tariffs = repository.findAll();
        return tariffs.stream().collect(Collectors.toMap(Tariff::getJobType, t -> t));
    }

}
