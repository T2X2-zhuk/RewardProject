package rewardCalculationTest;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import rewardCalculation.dto.TariffDTO;
import rewardCalculation.requests.CommonRequestForTariffParameters;
import static org.hamcrest.Matchers.equalTo;
import static io.restassured.RestAssured.given;

public class TariffControllerAcceptanceTest {

    private static final String BASE_URL_REWARD_CALCULATION_APP = "http://localhost:8080";

    //Test PASSED!
    //@Test
    public void acceptanceTest(){
        CleanRewardDbAndPaymentDatabaseForTest.rewardCalculationCleanDb(true,true,true);
        //Successful
        Response createResponse = createTariff("speech",4.0);
        createResponse.then().statusCode(200);
        Long tariffId = createResponse.jsonPath().getLong("tariffDTO.id");
        getTariff(tariffId).then().statusCode(200).body("tariffDTO.jobType", equalTo("speech"));

        //Unsuccessful
        createTariff(null,0.0).then().statusCode(200)
                .body("errors[0].errorCode",equalTo("ERROR_CODE_For_Tariff_1"))
                .body("errors[1].errorCode",equalTo("ERROR_CODE_For_Tariff_3"));
        createTariff("speech",4.0).then().statusCode(200)
                .body("errors[0].errorCode",equalTo("ERROR_CODE_For_Tariff_6"));
        getTariff(tariffId + 1L).then().statusCode(200).body("errors[0].errorCode",equalTo("ERROR_CODE_For_Tariff_2"));
    }

    public static Response createTariff(String jobType, double amount) {
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
