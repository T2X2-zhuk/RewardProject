package rewardCalculationTest;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import rewardCalculation.dto.RewardDTO;
import rewardCalculation.requests.CommonRequestForRewardParameters;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RewardRestControllerAcceptanceTest {

    private static final String BASE_URL_REWARD_CALCULATION_APP = "http://localhost:8080";
    //Test PASSED!
    //@Test
    public void acceptanceTest(){
        CleanRewardDbAndPaymentDatabaseForTest.rewardCalculationCleanDb(true,true,true);
        TariffControllerAcceptanceTest.createTariff("speech",29.29);
        //Unsuccessful
        EmployeeRestControllerAcceptanceTest.createEmployee
                (null,"Lasamanba",3.5).then().statusCode(200);
        createReward(null,null)
                .then().statusCode(200)
                .body("errors[0].errorCode",equalTo("ERROR_CODE_For_Reward_2"))
                .body("errors[1].errorCode",equalTo("ERROR_CODE_For_Tariff_1"));
        createReward(1L,"null")
                .then().statusCode(200)
                .body("errors[0].errorCode",equalTo("ERROR_CODE_For_Tariff_4"));
        //Successful

        Response response =  EmployeeRestControllerAcceptanceTest.createEmployee
                ("Harbon","Lasamanba",3.5);
        response.then().statusCode(200);
        Long employeeId = response.jsonPath().getLong("employeeDTO.id");

        Response rewardResponse = createReward(employeeId,"speech");
        rewardResponse.then().statusCode(200);
        Long  rewardId = rewardResponse.jsonPath().getLong("rewardDTO.id");

        getReward(rewardId).then().statusCode(200).body("rewardDTO.id",equalTo(rewardId.intValue()))
                .body("rewardDTO.employeeId", equalTo(employeeId.intValue()))
                .body("rewardDTO.jobType",equalTo("speech"));
    }

    public static Response createReward(Long employeeId,String jobType) {
        return given()
                .baseUri(BASE_URL_REWARD_CALCULATION_APP)
                .contentType("application/json")
                .body(CommonRequestForRewardParameters.builder()
                        .rewardDTO(RewardDTO.builder().employeeId(employeeId).jobType(jobType).build()).build())
                .post("/reward/calculation/createReward");
    }

    public static Response getReward(Long rewardId){
        return given()
                .baseUri(BASE_URL_REWARD_CALCULATION_APP)
                .contentType("application/json")
                .pathParam("id", rewardId)
                .get("/reward/calculation/getReward/{id}");
    }
}
