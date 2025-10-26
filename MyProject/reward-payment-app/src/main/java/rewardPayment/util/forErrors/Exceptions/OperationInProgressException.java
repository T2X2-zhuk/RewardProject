package rewardPayment.util.forErrors.Exceptions;

public class OperationInProgressException extends RuntimeException{

    public OperationInProgressException(String lockName) {
        super("⏳ Операция \"" + lockName + "\" уже выполняется.");
    }
}
