package RewardCalculation.rest;
import RewardCalculation.dto.TariffDTO;
import RewardCalculation.requests.CommonRequestForTariffParameters;
import RewardCalculation.responses.CommonResponseForTariffParameters;
import RewardCalculation.servises.tariff.CreateTariffService;
import RewardCalculation.servises.tariff.GetTariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test/tariff")
public class TariffController {

    @Autowired private CreateTariffService createTariffService;
    @Autowired private GetTariffService getTariffService;

    @PostMapping(path = "/createTariff",
            consumes = "application/json",
            produces = "application/json")
    public CommonResponseForTariffParameters createTariff(@RequestBody CommonRequestForTariffParameters request) {
       return createTariffService.execute(request);
    }

    @GetMapping(path = "/getTariff/{id}",
            produces = "application/json")
    public CommonResponseForTariffParameters getTariff(@PathVariable Long id) {
        CommonRequestForTariffParameters request = CommonRequestForTariffParameters.builder()
                .tariffDTO(TariffDTO.builder().id(id).build()).build();
        return getTariffService.execute(request);
    }
}
