package test.loadTest;

import test.classesWithRestTestsMethod.rewardCalculationApp.ClassWithMethodsForOutBoxDispatchers;
import test.classesWithRestTestsMethod.rewardCalculationApp.RewardClassWithMethodsForAcceptanceTests;

public class PerformingTheCalculationOfRewards {

    public static void execute() {
        for (int i = 0; i < 4; i++){
            RewardClassWithMethodsForAcceptanceTests.calculate().then().statusCode(200);
        }
        // Outbox dispatcher (если используется)
         ClassWithMethodsForOutBoxDispatchers.outBoxDispatcher();
    }
}
