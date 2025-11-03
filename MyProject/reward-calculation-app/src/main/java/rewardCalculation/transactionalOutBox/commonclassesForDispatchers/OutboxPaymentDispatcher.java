package rewardCalculation.transactionalOutBox.commonclassesForDispatchers;

public interface OutboxPaymentDispatcher {

    void dispatchPendingPayments();
}
