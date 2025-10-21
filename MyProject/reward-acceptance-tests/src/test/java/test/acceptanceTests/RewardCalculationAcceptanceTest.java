package test.acceptanceTests;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.DTOS.JobTypeDTO;
import test.classesWithRestTestsMethod.paymentApp.CleanPaymentDbForTest;
import test.classesWithRestTestsMethod.paymentApp.PaymentClassWithMethodsForAcceptanceTests;
import test.classesWithRestTestsMethod.rewardCalculationApp.*;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RewardCalculationAcceptanceTest {

    //@BeforeEach
    public void cleanDB(){
        CleanRewardDbForTest.rewardCalculationCleanDb(true,true,true,true,true);
        CleanPaymentDbForTest.rewardPaymentCleanDb(true);
    }
    //Test PASSED!
    //@Test
    public void successfulTest(){
        JobTypeClassWithRestMethodsForAcceptanceTests.createJobType(JobTypeDTO.builder().jobType("Speech").build());
        TariffClassWithMethodsForAcceptanceTests.createTariff("speech",new BigDecimal("23.24"));
        Response employee = EmployeeClassWithRestMethodsForAcceptanceTests.createEmployee
                ("Harbon","Lasamanba",new BigDecimal("3.5"));
        Long employeeId = employee.jsonPath().getLong("employeeDTO.id");
        // 2. Создаём награду
        RewardClassWithMethodsForAcceptanceTests.createReward(employeeId,"speech");

        // 3. Делаем расчет
        Response paymentResponse = RewardClassWithMethodsForAcceptanceTests.calculate();
        paymentResponse.then().statusCode(200).body("successfulSaving",equalTo(true));

        PaymentClassWithMethodsForAcceptanceTests.getPayments(employeeId).then().statusCode(200)
                .body("paymentDTOS[0].employeeId",equalTo(employeeId.intValue()))
                .body("paymentDTOS[0].amount",equalTo(104.58F));
    }

    //Test PASSED!
    //@Test
    public void acceptanceTest2(){
        RewardClassWithMethodsForAcceptanceTests.calculate().then().statusCode(200)
                .body("errors[0].errorCode", equalTo("ERROR_CODE_FOR_REWARD_1"));
    }

}
