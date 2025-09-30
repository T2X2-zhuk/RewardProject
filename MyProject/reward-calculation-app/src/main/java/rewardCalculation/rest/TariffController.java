package rewardCalculation.rest;
import lombok.RequiredArgsConstructor;
import rewardCalculation.dto.TariffDTO;
import rewardCalculation.requests.CommonRequestForTariffParameters;
import rewardCalculation.responses.CommonResponseForTariffParameters;
import rewardCalculation.servises.tariff.CreateTariffService;
import rewardCalculation.servises.tariff.GetTariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test/tariff")
@RequiredArgsConstructor
public class TariffController {

    private final CreateTariffService createTariffService;
    private final GetTariffService getTariffService;

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
