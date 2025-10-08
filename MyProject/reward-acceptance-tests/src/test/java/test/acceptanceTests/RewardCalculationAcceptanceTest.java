package test.acceptanceTests;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import test.classesWithRestTestsMethod.paymentApp.CleanPaymentDbForTest;
import test.classesWithRestTestsMethod.paymentApp.PaymentClassWithMethodsForAcceptanceTests;
import test.classesWithRestTestsMethod.rewardCalculationApp.CleanRewardDbForTest;
import test.classesWithRestTestsMethod.rewardCalculationApp.EmployeeClassWithRestMethodsForAcceptanceTests;
import test.classesWithRestTestsMethod.rewardCalculationApp.RewardClassWithMethodsForAcceptanceTests;
import test.classesWithRestTestsMethod.rewardCalculationApp.TariffClassWithMethodsForAcceptanceTests;

import java.math.BigDecimal;
import java.util.Objects;

import static org.hamcrest.Matchers.equalTo;

public class RewardCalculationAcceptanceTest {

    //@BeforeEach
    public void cleanDB(){
        CleanRewardDbForTest.rewardCalculationCleanDb(true,true,true);
        CleanPaymentDbForTest.rewardPaymentCleanDb(true);
    }
    //Test PASSED!
    //@Test
    public void acceptanceTest(){
        TariffClassWithMethodsForAcceptanceTests.createTariff("speech",new BigDecimal("23.24")).then().statusCode(200);

        // 1. Создаём сотрудника
        Response employeeResponse = EmployeeClassWithRestMethodsForAcceptanceTests.createEmployee("Иван","Иванов",new BigDecimal("1.2"));
        employeeResponse.then().statusCode(200);
        Long employeeId = employeeResponse.jsonPath().getLong("employeeDTO.id");

        // 2. Создаём награду
        RewardClassWithMethodsForAcceptanceTests.createReward(employeeId,"speech");

        // 3. Делаем расчет
        Response paymentResponse = RewardClassWithMethodsForAcceptanceTests.calculate();
        paymentResponse.then().statusCode(200).body("successfulSaving",equalTo(true));

       // 4. Проверяем полученный результат
        PaymentClassWithMethodsForAcceptanceTests.getPayment(employeeId,new BigDecimal("51.13")).then()
                .statusCode(200)
                .body("paymentDTOS[0].employeeId", equalTo(employeeId.intValue()))
                .body("paymentDTOS[0].amount", equalTo(51.13F));
    }

    //Test PASSED!
    //@Test
    public void acceptanceTest2(){
        TariffClassWithMethodsForAcceptanceTests.createTariff("speech",new BigDecimal("23.24")).then().statusCode(200);

        RewardClassWithMethodsForAcceptanceTests.calculate().then().statusCode(200)
                .body("errors[0].errorCode", equalTo("ERROR_CODE_For_Employee_6"));
        // 1. Создаём сотрудника
        Response employeeResponse = EmployeeClassWithRestMethodsForAcceptanceTests.createEmployee("Иван","Иванов",new BigDecimal("1.2"));
        employeeResponse.then().statusCode(200);
        Long employeeId = employeeResponse.jsonPath().getLong("employeeDTO.id");

        RewardClassWithMethodsForAcceptanceTests.calculate().then().statusCode(200).body("errors[0].errorCode",equalTo("ERROR_CODE_For_Reward_1"));

        PaymentClassWithMethodsForAcceptanceTests.getPayment(employeeId,new BigDecimal("23.24")).then().statusCode(200).body("errors[0].errorCode",equalTo("ERROR_CODE_FOR_PAYMENT_3"));
    }

}
