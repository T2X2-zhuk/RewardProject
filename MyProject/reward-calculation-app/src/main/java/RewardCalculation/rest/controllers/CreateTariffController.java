package RewardCalculation.rest.controllers;
import RewardCalculation.requests.CreateTariffRequest;
import RewardCalculation.responses.CreateTariffResponse;
import RewardCalculation.service.interfaces.CreateTariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/tariff")
public class CreateTariffController {

    @Autowired private CreateTariffService service;
    @PostMapping(path = "/",
            consumes = "application/json",
            produces = "application/json")
    public CreateTariffResponse createTariff(@RequestBody CreateTariffRequest request) {
       return service.execute(request);
    }
}
