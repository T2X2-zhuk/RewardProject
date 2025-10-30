package test.acceptanceTests;

import io.restassured.response.Response;
import test.DTOS.JobTypeDTO;
import test.classesWithRestTestsMethod.paymentApp.CleanPaymentDbForTest;
import test.classesWithRestTestsMethod.paymentApp.PaymentClassWithMethodsForAcceptanceTests;
import test.classesWithRestTestsMethod.rewardCalculationApp.*;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;

public class RewardCalculationAcceptanceTest {

    //@BeforeEach
    public void cleanDB(){
        CleanRewardDbForTest.rewardCalculationCleanDb(true,true,true,true,true);
        CleanPaymentDbForTest.rewardPaymentCleanDb(true);
    }
    //Test PASSED Work when OutBox is false!
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
   // @Test
    public void acceptanceTest2(){
        RewardClassWithMethodsForAcceptanceTests.calculate().then().statusCode(200)
                .body("errors[0].errorCode", equalTo("ERROR_CODE_FOR_REWARD_1"));
    }

    //Test validation working OutBoxPaymentDispatcher when OutBox is true PASSED!
    //@Test
    public void acceptanceTestOutBoxDispatcher(){
        // 1. Делаем тип работы , тариф и сотрудника
        JobTypeClassWithRestMethodsForAcceptanceTests.createJobType(JobTypeDTO.builder().jobType("Speech").build());
        TariffClassWithMethodsForAcceptanceTests.createTariff("speech",new BigDecimal("23.24"));
        Response employee = EmployeeClassWithRestMethodsForAcceptanceTests.createEmployee
                ("Harbon","Lasamanba",new BigDecimal("3.5"));
        Long employeeId = employee.jsonPath().getLong("employeeDTO.id");
        // 2. Создаём награду
        Response response = RewardClassWithMethodsForAcceptanceTests.createReward(employeeId,"speech");
        Long rewardId = response.jsonPath().getLong("rewardDTO.id");
        // 3. Делаем расчет
        Response paymentResponse = RewardClassWithMethodsForAcceptanceTests.calculate();
        paymentResponse.then().statusCode(200).body("successfulSaving",equalTo(true));
        ClassWithMethodsForOutBoxDispatchers.outBoxDispatcher().then().statusCode(200);

        RewardClassWithMethodsForAcceptanceTests.getReward(rewardId)
                .then()
                .statusCode(200)
                .body("rewardDTO.status", equalTo("PAID"));

        PaymentClassWithMethodsForAcceptanceTests.getPayments(employeeId).then().statusCode(200)
                .body("paymentDTOS[0].employeeId",equalTo(employeeId.intValue()))
                .body("paymentDTOS[0].amount",equalTo(104.58F));
    }

}
