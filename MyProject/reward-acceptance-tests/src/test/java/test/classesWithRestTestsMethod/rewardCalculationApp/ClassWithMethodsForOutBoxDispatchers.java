package test.classesWithRestTestsMethod.rewardCalculationApp;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ClassWithMethodsForOutBoxDispatchers {

    private static final String BASE_URL_REWARD_CALCULATION_APP = "http://localhost:8080";

    public static Response outBoxDispatcher(){
        return  given()
                .baseUri(BASE_URL_REWARD_CALCULATION_APP)
                .contentType("application/json")
                .post("/api/test/dispatch");
    }
}
