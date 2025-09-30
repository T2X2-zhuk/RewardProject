package test.classesWithRestTestsMethod.paymentApp;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PaymentClassWithMethodsForAcceptanceTests {

    private static final String Base_URL_REWARD_PAYMENT_APP = "http://localhost:8095";

    public static Response getPayment(Long employeeId, double amount){
        return given()
                .baseUri(Base_URL_REWARD_PAYMENT_APP)
                .contentType("application/json")
                .queryParam("employeeId", employeeId)
                .queryParam("amount", amount)
                .get("/reward/payment/searchPayment");
    }

}
