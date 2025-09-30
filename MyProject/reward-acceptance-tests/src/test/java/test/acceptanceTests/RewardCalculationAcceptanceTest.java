package test.acceptanceTests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import test.classesWithRestTestsMethod.paymentApp.CleanPaymentDbForTest;
import test.classesWithRestTestsMethod.paymentApp.PaymentClassWithMethodsForAcceptanceTests;
import test.classesWithRestTestsMethod.rewardCalculationApp.CleanRewardDbForTest;
import test.classesWithRestTestsMethod.rewardCalculationApp.EmployeeClassWithRestMethodsForAcceptanceTests;
import test.classesWithRestTestsMethod.rewardCalculationApp.RewardClassWithMethodsForAcceptanceTests;
import test.classesWithRestTestsMethod.rewardCalculationApp.TariffClassWithMethodsForAcceptanceTests;

import static org.hamcrest.Matchers.equalTo;

public class RewardCalculationAcceptanceTest {

    //Test PASSED!
    //@Test
    public void acceptanceTest(){
        CleanRewardDbForTest.rewardCalculationCleanDb(true,true,true);
        CleanPaymentDbForTest.rewardPaymentCleanDb(true);
        TariffClassWithMethodsForAcceptanceTests.createTariff("speech",23.24).then().statusCode(200);

        // 1. Создаём сотрудника
        Response employeeResponse = EmployeeClassWithRestMethodsForAcceptanceTests.createEmployee("Иван","Иванов",1.2);
        employeeResponse.then().statusCode(200);
        Long employeeId = employeeResponse.jsonPath().getLong("employeeDTO.id");

        // 2. Создаём награду
        RewardClassWithMethodsForAcceptanceTests.createReward(employeeId,"speech");

        // 3. Делаем расчет
        Response paymentResponse = RewardClassWithMethodsForAcceptanceTests.calculate();
        paymentResponse.then().statusCode(200);
        Long paymentId = paymentResponse.jsonPath().getLong("paymentDTOS[0].id");
        Double amount = paymentResponse.jsonPath().getDouble("paymentDTOS[0].amount");

       // 4. Проверяем полученный результат
        PaymentClassWithMethodsForAcceptanceTests.getPayment(employeeId,amount).then()
                .statusCode(200)
                .body("paymentDTO.id", equalTo(paymentId.intValue()))
                .body("paymentDTO.employeeId", equalTo(employeeId.intValue()))
                .body("paymentDTO.amount", equalTo(51.128F));
    }

    //Test PASSED!
    //@Test
    public void acceptanceTest2(){
        CleanRewardDbForTest.rewardCalculationCleanDb(true,true,true);
        TariffClassWithMethodsForAcceptanceTests.createTariff("speech",23.24).then().statusCode(200);

        RewardClassWithMethodsForAcceptanceTests.calculate().then().statusCode(200)
                .body("errors[0].errorCode", equalTo("ERROR_CODE_For_Employee_6"));
        // 1. Создаём сотрудника
        Response employeeResponse = EmployeeClassWithRestMethodsForAcceptanceTests.createEmployee("Иван","Иванов",1.2);
        employeeResponse.then().statusCode(200);
        Long employeeId = employeeResponse.jsonPath().getLong("employeeDTO.id");

        RewardClassWithMethodsForAcceptanceTests.calculate().then().statusCode(200).body("errors[0].errorCode",equalTo("ERROR_CODE_For_Reward_1"));

        PaymentClassWithMethodsForAcceptanceTests.getPayment(employeeId,23.24).then().statusCode(200).body("errors[0].errorCode",equalTo("ERROR_CODE_FOR_PAYMENT_3"));
    }




}
