package test.classesWithRestTestsMethod.paymentApp;

import io.restassured.response.Response;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static io.restassured.RestAssured.given;

public class PaymentClassWithMethodsForAcceptanceTests {

    private static final String Base_URL_REWARD_PAYMENT_APP = "http://localhost:8095";

    public static Response getPayment(Long employeeId, BigDecimal amount){
        return given()
                .baseUri(Base_URL_REWARD_PAYMENT_APP)
                .contentType("application/json")
                .queryParam("employeeId", employeeId)
                .queryParam("amount", amount.setScale(2, RoundingMode.HALF_UP))
                .get("/reward/payment/searchPayment");
    }

}
