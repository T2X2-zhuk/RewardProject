package test.classesWithRestTestsMethod.rewardCalculationApp;


import test.cleanDb.rewardCalculation.CleanRewardDbRequest;

import static io.restassured.RestAssured.given;

public class CleanRewardDbForTest {

    private static final String BASE_URL_REWARD_CALCULATION_APP = "http://localhost:8080";


    public static void rewardCalculationCleanDb(boolean cleanEmployee, boolean cleanReward, boolean cleanTariff) {
        CleanRewardDbRequest request = CleanRewardDbRequest.builder().cleanEmployee(cleanEmployee)
                .cleanReward(cleanReward).cleanTariff(cleanTariff).build();
        given()
                .baseUri(BASE_URL_REWARD_CALCULATION_APP)
                .contentType("application/json")
                .body(request)
                .post("/api/test/rewardDb/cleanDb")
                .then()
                .statusCode(200);
        //"%s" -> для строковых переменных;
        //%f -> для double переменных;
        //%d -> int, long;
        //%b -> boolean;
    }

}
