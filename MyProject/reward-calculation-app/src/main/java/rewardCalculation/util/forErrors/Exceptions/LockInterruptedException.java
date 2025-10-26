package rewardCalculation.util.forErrors.Exceptions;

public class LockInterruptedException extends RuntimeException{

    public LockInterruptedException(String message, InterruptedException cause) {
        super(message, cause);
    }
}
