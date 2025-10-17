package RewardPayment.rest.cleandb;

import RewardPayment.services.CleanDbService;
import RewardPayment.util.forServices.RewardExecutionLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/paymentDb")
@RequiredArgsConstructor
@Slf4j
public class CleanPaymentDbController {

    private final CleanDbService cleanDbService;
    private final RewardExecutionLock rewardExecutionLock;

    @PostMapping(path = "/clean",
            consumes = "application/json",
            produces = "application/json")
    public CleanPaymentDbResponse cleanDb(@RequestBody CleanPaymentDbRequest request) {
        return rewardExecutionLock.runWithLock(()-> {
            log.info("{} is start!",this.getClass().getSimpleName());
            CleanPaymentDbResponse response = cleanDbService.execute(request);
            log.info("{} is execute!",this.getClass().getSimpleName());
            return response;
        });
    }
}
