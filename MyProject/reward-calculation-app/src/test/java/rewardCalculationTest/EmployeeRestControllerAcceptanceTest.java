package rewardCalculationTest;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import rewardCalculation.dto.EmployeeDTO;
import rewardCalculation.requests.CommonRequestForEmployeeParameters;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class EmployeeRestControllerAcceptanceTest {

    private static final String BASE_URL_REWARD_CALCULATION_APP = "http://localhost:8080";

    //@Test Test PASSED!
    public void acceptanceTest(){
        CleanRewardDbAndPaymentDatabaseForTest.rewardCalculationCleanDb(true,true,true);
       //Unsuccessful
        createEmployee
                (null,null,0.0).
                then().statusCode(200).body("errors[0].errorCode" ,
                        equalTo("ERROR_CODE_For_Employee_1"))
                .body("errors[1].errorCode",
                        equalTo("ERROR_CODE_For_Employee_2"))
                .body("errors[2].errorCode",equalTo("ERROR_CODE_For_Employee_3"));

        getEmployee(2L).then().statusCode(200).body("errors[0].errorCode",equalTo("ERROR_CODE_For_Employee_5"));
        //Successful
        Response response = createEmployee
                ("Harbon","Lasamanba",3.5);
        response.then().statusCode(200);
        Long employeeId = response.jsonPath().getLong("employeeDTO.id");

        getEmployee(employeeId).then().statusCode(200).body("employeeDTO.firstName",equalTo("Harbon"))
                .body("employeeDTO.lastName",equalTo("Lasamanba"))
                .body("employeeDTO.bonusCoefficient",equalTo(3.5F));
    }

    public static Response createEmployee(String firstName, String lastName, double bonusCoefficient) {
        EmployeeDTO dto = EmployeeDTO.builder().firstName(firstName)
                .lastName(lastName).bonusCoefficient(bonusCoefficient).build();
        return given()
                .baseUri(BASE_URL_REWARD_CALCULATION_APP)
                .contentType("application/json")
                .body(CommonRequestForEmployeeParameters.builder().employeeDTO(dto).build())
                .post("/api/test/employee/createEmployee");
        //"%s" -> для строковых переменных;
        //%f -> для double переменных;
        //%d -> int, long;
        //%b -> boolean;\
    }

    public static Response getEmployee(Long employeeId){
        return given()
                .baseUri(BASE_URL_REWARD_CALCULATION_APP)
                .contentType("application/json")
                .pathParam("id", employeeId)
                .get("/api/test/employee/getEmployee/{id}");
    }
}
