package rewardCalculationTest;

import io.restassured.response.Response;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.empty;

public class RewardCalculationTest {

    private static final String BASE_URL = "http://localhost:8080";

    private void cleanDb() {
        given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body("{\"cleanEmployee\":true,\"cleanReward\":true,\"cleanTariff\":true}")
                .post("/api/test/rewardDb/cleanDb")
                .then()
                .statusCode(200);
    }

    //@Test
    public void rewardCalculationExecute_shouldReturnNonEmptyPaymentList() {
        cleanDb();

        // 1. Создаём сотрудника
        Response employeeResponse = given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body("""
                      {
                          "employeeDTO": {
                              "firstName": "Иван",
                              "lastName": "Иванов",
                              "bonusCoefficient": 1.2
                          }
                      }
                      """)
                .post("/api/test/employee/createEmployee");

        employeeResponse.then().statusCode(200);
        Long employeeId = employeeResponse.jsonPath().getLong("employeeDTO.id");

        // 2. Создаём награду
        given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body("""
                      {
                          "rewardDTO": {
                              "employeeId": %d,
                              "jobType": "speech"
                          }
                      }
                      """.formatted(employeeId))
                .post("/reward/calculation/createReward")
                .then()
                .statusCode(200);

        // 3. Запускаем расчёт
        given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .post("/reward/calculation/rewardCalculationExecute")
                .then()
                .statusCode(200)
                .body("paymentDTOS", not(empty()));
    }

}
