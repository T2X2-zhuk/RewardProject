package rewardCalculation.transactionalOutBox.dispatchers;

public interface OutboxPaymentDispatcher {

    void dispatchPendingPayments();
}
