package test.classesWithRestTestsMethod.rewardCalculationApp;

import io.restassured.response.Response;
import test.DTOS.EmployeeDTO;
import test.rewardapp.requests.CommonRequestForEmployeeParameters;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class EmployeeClassWithRestMethodsForAcceptanceTests {

    private static final String BASE_URL_REWARD_CALCULATION_APP = "http://localhost:8080";

    public static Response createEmployee(String firstName, String lastName, BigDecimal bonusCoefficient) {
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
