package rewardCalculationTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class RewardCalculationAcceptanceTest {

    private static final String BASE_URL_REWARD_CALCULATION_APP = "http://localhost:8080";
    private static final String Base_URL_REWARD_PAYMENT_APP = "http://localhost:8095";

    //Test PASSED!
    //@Test
    public void acceptanceTest(){
        CleanRewardDbAndPaymentDatabaseForTest.rewardCalculationCleanDb(true,true,true);
        CleanRewardDbAndPaymentDatabaseForTest.rewardPaymentCleanDb(true);
        TariffControllerAcceptanceTest.createTariff("speech",23.24).then().statusCode(200);

        // 1. Создаём сотрудника
        Response employeeResponse = EmployeeRestControllerAcceptanceTest.createEmployee("Иван","Иванов",1.2);
        employeeResponse.then().statusCode(200);
        Long employeeId = employeeResponse.jsonPath().getLong("employeeDTO.id");

        // 2. Создаём награду
        RewardRestControllerAcceptanceTest.createReward(employeeId,"speech");

        // 3. Делаем расчет
        Response paymentResponse = calculate();
        paymentResponse.then().statusCode(200);
        Long paymentId = paymentResponse.jsonPath().getLong("paymentDTOS[0].id");
        Double amount = paymentResponse.jsonPath().getDouble("paymentDTOS[0].amount");

       // 4. Проверяем полученный результат
        getPayment(employeeId,amount).then()
                .statusCode(200)
                .body("paymentDTO.id", equalTo(paymentId.intValue()))
                .body("paymentDTO.employeeId", equalTo(employeeId.intValue()))
                .body("paymentDTO.amount", equalTo(51.128F));
    }

    //Test PASSED!
    //@Test
    public void acceptanceTest2(){
        CleanRewardDbAndPaymentDatabaseForTest.rewardCalculationCleanDb(true,true,true);
        TariffControllerAcceptanceTest.createTariff("speech",23.24).then().statusCode(200);

        calculate().then().statusCode(200)
                .body("errors[0].errorCode", equalTo("ERROR_CODE_For_Employee_6"));
        // 1. Создаём сотрудника
        EmployeeRestControllerAcceptanceTest.createEmployee("Иван","Иванов",1.2).then().statusCode(200);

        calculate().then().statusCode(200).body("errors[0].errorCode",equalTo("ERROR_CODE_For_Reward_1"));
    }

    private static Response calculate() {
        // 3. Запускаем расчёт
        return  given()
                .baseUri(BASE_URL_REWARD_CALCULATION_APP)
                .contentType("application/json")
                .post("/reward/calculation/rewardCalculationExecute");
    }

    private static Response  getPayment(Long employeeId, double amount){
        return given()
                .baseUri(Base_URL_REWARD_PAYMENT_APP)
                .contentType("application/json")
                .queryParam("employeeId", employeeId)
                .queryParam("amount", amount)
                .get("/reward/payment/searchPayment");
    }
}
