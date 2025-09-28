package rewardCalculationTest;


import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class RewardCalculationAcceptanceTest {

    private static final String BASE_URL_REWARD_CALCULATION_APP = "http://localhost:8080";
    private static final String Base_URL_REWARD_PAYMENT_APP = "http://localhost:8095";

    private void rewardCalculationCleanDb() {
        given()
                .baseUri(BASE_URL_REWARD_CALCULATION_APP)
                .contentType("application/json")
                .body("""
                        {"cleanEmployee":true,"cleanReward":true,"cleanTariff":true}
                        """)
                .post("/api/test/rewardDb/cleanDb")
                .then()
                .statusCode(200);
    }

    private void rewardPaymentCleanDb() {
        given()
                .baseUri(Base_URL_REWARD_PAYMENT_APP)
                .contentType("application/json")
                .body("""
                        {"cleanPayment":true}""")
                .post("/api/test/paymentDb/clean")
                .then()
                .statusCode(200);
    }

   // @Test
    public void rewardCalculationExecute_shouldReturnNonEmptyPaymentList() throws JsonProcessingException {
        rewardCalculationCleanDb();
        rewardPaymentCleanDb();
        createTariff();

        // 1. Создаём сотрудника
        Response employeeResponse = createEmployee();
        employeeResponse.then().statusCode(200);
        Long employeeId = employeeResponse.jsonPath().getLong("employeeDTO.id");

        // 2. Создаём награду
        createReward(employeeId);

        // 3. Делаем расчет
        Response paymentResponse = calculate();
        paymentResponse.then().statusCode(200);
        Long paymentId = paymentResponse.jsonPath().getLong("paymentDTOS[0].id");
       // 4. Проверяем полученный результат
        getPayment(employeeId).then()
                .statusCode(200)
                .body("paymentDTO.id", equalTo(paymentId.intValue()))
                .body("paymentDTO.employeeId", equalTo(employeeId.intValue()))
                .body("paymentDTO.amount", equalTo(46.706F));
    }

    private  Response  calculate() {
        // 3. Запускаем расчёт
        return (Response) given()
                .baseUri(BASE_URL_REWARD_CALCULATION_APP)
                .contentType("application/json")
                .post("/reward/calculation/rewardCalculationExecute");
    }

    private static void createReward(Long employeeId) {
        given()
                .baseUri(BASE_URL_REWARD_CALCULATION_APP)
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
    }

    private static void createTariff() {
        given()
                .baseUri(BASE_URL_REWARD_CALCULATION_APP)
                .contentType("application/json")
                .body("""
                      {
                          "tariffDTO": {
                              "jobType": "speech",
                              "amount": 21.23
                           
                          }
                      }
                      """)
                .post("/api/test/tariff/createTariff");
    }

    private Response createEmployee(){
       return given()
                .baseUri(BASE_URL_REWARD_CALCULATION_APP)
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
    }

    private Response  getPayment(Long employeeId){
         return given()
                .baseUri(Base_URL_REWARD_PAYMENT_APP)
                .contentType("application/json")
                .get("/reward/payment/searchPayment?employeeId=%d&amount=46.706".formatted(employeeId));
    }
}
