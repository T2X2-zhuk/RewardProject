package rewardCalculationTest;

import rewardCalculation.rest.cleandb.CleanRewardDbRequest;

import static io.restassured.RestAssured.authentication;
import static io.restassured.RestAssured.given;

public class CleanRewardDbAndPaymentDatabaseForTest {

    private static final String BASE_URL_REWARD_CALCULATION_APP = "http://localhost:8080";
    private static final String Base_URL_REWARD_PAYMENT_APP = "http://localhost:8095";

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

    public static void rewardPaymentCleanDb(boolean cleanPayment) {
        given()
                .baseUri(Base_URL_REWARD_PAYMENT_APP)
                .contentType("application/json")
                .body("""
                        {"cleanPayment":%b}""".formatted(cleanPayment))
                .post("/api/test/paymentDb/clean")
                .then()
                .statusCode(200);
        //"%s" -> для строковых переменных;
        //%f -> для double переменных;
        //%d -> int, long;
        //%b -> boolean;
    }

}
