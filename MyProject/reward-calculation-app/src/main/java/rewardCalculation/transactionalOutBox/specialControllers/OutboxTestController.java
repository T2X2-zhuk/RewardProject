package rewardCalculation.transactionalOutBox.specialControllers;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rewardCalculation.transactionalOutBox.dispatchers.OutboxPaymentDispatcher;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@ConditionalOnExpression("${app.transactionalOutBox:true} == true")
public class OutboxTestController {

    private final OutboxPaymentDispatcher dispatcher;


    @PostMapping("/dispatch")
    public ResponseEntity<String> triggerDispatch() {
        dispatcher.dispatchPendingPayments();
        return ResponseEntity.ok("Dispatcher executed");
    }
}
