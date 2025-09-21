package RewardCalculation.rest.tariff;
import RewardCalculation.responses.GetTariffResponse;
import RewardCalculation.service.interfaces.GetTariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/tariff")
public class GetTariffController {
    @Autowired private GetTariffService service;

    @GetMapping(path = "/{id}",
            produces = "application/json")
    public GetTariffResponse getTariff(@PathVariable Long id) {
        return service.execute(id);
    }
}
