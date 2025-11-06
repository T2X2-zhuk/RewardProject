package rewardPayment.util.forErrors.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OperationInProgressException.class)
    public Mono<ResponseEntity<String>> handleLockConflict(OperationInProgressException ex) {
        return Mono.just(
                ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("{\"error\": \"" + ex.getMessage() + "\"}")
        );
    }

    @ExceptionHandler(LockInterruptedException.class)
    public Mono<ResponseEntity<String>> handleInterrupted(LockInterruptedException ex) {
        return Mono.just(
                ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"error\": \"" + ex.getMessage() + "\"}")
        );
    }
}
