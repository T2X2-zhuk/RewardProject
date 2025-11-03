package rewardCalculation.rest.controllers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import rewardCalculation.dto.TariffDTO;
import rewardCalculation.requests.CommonRequestForTariffParameters;
import rewardCalculation.responses.CommonResponseForTariffParameters;
import org.springframework.web.bind.annotation.*;
import rewardCalculation.services.tariff.CreateTariffService;
import rewardCalculation.services.tariff.GetTariffService;

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
        log.info("[{}] {} is start!", MDC.get("traceId"), this.getClass().getSimpleName());

        CommonResponseForTariffParameters response = createTariffService.execute(request);

        log.info("[{}] {} is execute!", MDC.get("traceId"), this.getClass().getSimpleName());

        return response;
    }

    @GetMapping(path = "/getTariff/{id}",
            produces = "application/json")
    public CommonResponseForTariffParameters getTariff(@PathVariable Long id) {
        log.info("[{}] {} is start!", MDC.get("traceId"), this.getClass().getSimpleName());

        CommonResponseForTariffParameters response = getTariffService.execute(CommonRequestForTariffParameters.builder()
                .tariffDTO(TariffDTO.builder().id(id).build()).build());

        log.info("[{}] {} is execute!", MDC.get("traceId"), this.getClass().getSimpleName());

        return response;
    }
}
