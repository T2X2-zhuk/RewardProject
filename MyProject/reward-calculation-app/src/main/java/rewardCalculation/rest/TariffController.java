package rewardCalculation.rest;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class TariffController {

    private final CreateTariffService createTariffService;
    private final GetTariffService getTariffService;

    @PostMapping(path = "/createTariff",
            consumes = "application/json",
            produces = "application/json")
    public CommonResponseForTariffParameters createTariff(@RequestBody CommonRequestForTariffParameters request) {
        log.info("{} is start!",this.getClass().getSimpleName());
        CommonResponseForTariffParameters response = createTariffService.execute(request);
        log.info("{} is execute!",this.getClass().getSimpleName());
        return response;
    }

    @GetMapping(path = "/getTariff/{id}",
            produces = "application/json")
    public CommonResponseForTariffParameters getTariff(@PathVariable Long id) {
        log.info("{} is start!",this.getClass().getSimpleName());
        CommonRequestForTariffParameters request = CommonRequestForTariffParameters.builder()
                .tariffDTO(TariffDTO.builder().id(id).build()).build();
        CommonResponseForTariffParameters response = getTariffService.execute(request);
        log.info("{} is execute!",this.getClass().getSimpleName());
        return response;
    }
}
