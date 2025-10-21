package test.classesWithRestTestsMethod.rewardCalculationApp;

import io.restassured.response.Response;
import test.DTOS.EmployeeDTO;
import test.DTOS.JobTypeDTO;
import test.rewardapp.requests.CommonRequestForJobTypeParameters;
import static io.restassured.RestAssured.given;

public class JobTypeClassWithRestMethodsForAcceptanceTests {

    private static final String BASE_URL_REWARD_CALCULATION_APP = "http://localhost:8080";


    public static Response createJobType(JobTypeDTO jobTypeDTO) {
        return given()
                .baseUri(BASE_URL_REWARD_CALCULATION_APP)
                .contentType("application/json")
                .body(CommonRequestForJobTypeParameters.builder().jobTypeDTO(jobTypeDTO).build())
                .post("/api/test/job/type/createJobType");
        //"%s" -> для строковых переменных;
        //%f -> для double переменных;
        //%d -> int, long;
        //%b -> boolean;\
    }

    public static Response getJobType(Long id){
        return given()
                .baseUri(BASE_URL_REWARD_CALCULATION_APP)
                .contentType("application/json")
                .pathParam("id", id)
                .get("/api/test/job/type/getJobType/{id}");
    }
}
