package test.classesWithRestTestsMethod.rewardCalculationApp;

import io.restassured.response.Response;
import test.DTOS.TariffDTO;
import test.rewardapp.requests.CommonRequestForTariffParameters;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TariffClassWithMethodsForAcceptanceTests {

    private static final String BASE_URL_REWARD_CALCULATION_APP = "http://localhost:8080";

    public static Response createTariff(String jobType, BigDecimal amount) {
        CommonRequestForTariffParameters request = CommonRequestForTariffParameters.builder()
                .tariffDTO(TariffDTO.builder()
                        .jobType(jobType)
                        .amount(amount).build()).build();
        return given()
                .baseUri(BASE_URL_REWARD_CALCULATION_APP)
                .contentType("application/json")
                .body(request)
                .post("/api/test/tariff/createTariff");
    }

    public static Response getTariff(Long tariffId){
        return given()
                .baseUri(BASE_URL_REWARD_CALCULATION_APP)
                .contentType("application/json")
                .pathParam("id", tariffId)
                .get("/api/test/tariff/getTariff/{id}");
    }

}
