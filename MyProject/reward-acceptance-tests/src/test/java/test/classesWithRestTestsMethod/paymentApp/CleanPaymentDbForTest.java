package test.classesWithRestTestsMethod.paymentApp;

import static io.restassured.RestAssured.given;

public class CleanPaymentDbForTest {

    private static final String Base_URL_REWARD_PAYMENT_APP = "http://localhost:8095";

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
