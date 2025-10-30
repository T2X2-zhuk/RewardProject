package test.loadTest;

import test.classesWithRestTestsMethod.rewardCalculationApp.RewardClassWithMethodsForAcceptanceTests;

public class PerformingTheCalculationOfRewards {

    public static void execute() {
        RewardClassWithMethodsForAcceptanceTests.calculate().then().statusCode(200);
        RewardClassWithMethodsForAcceptanceTests.calculate().then().statusCode(200);;
        RewardClassWithMethodsForAcceptanceTests.calculate().then().statusCode(200);;
        RewardClassWithMethodsForAcceptanceTests.calculate().then().statusCode(200);;
        // Outbox dispatcher (если используется)
//        ClassWithMethodsForOutBoxDispatchers.outBoxDispatcher();
    }
}
