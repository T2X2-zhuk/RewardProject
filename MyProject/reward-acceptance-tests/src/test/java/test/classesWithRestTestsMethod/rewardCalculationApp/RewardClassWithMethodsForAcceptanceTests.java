package test.classesWithRestTestsMethod.rewardCalculationApp;

import io.restassured.response.Response;
import test.DTOS.RewardDTO;
import test.rewardapp.requests.CommonRequestForRewardParameters;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RewardClassWithMethodsForAcceptanceTests {

    private static final String BASE_URL_REWARD_CALCULATION_APP = "http://localhost:8080";

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

    public static Response calculate() {
        // 3. Запускаем расчёт
        return  given()
                .baseUri(BASE_URL_REWARD_CALCULATION_APP)
                .contentType("application/json")
                .post("/reward/calculation/rewardCalculationExecute");
    }
}
