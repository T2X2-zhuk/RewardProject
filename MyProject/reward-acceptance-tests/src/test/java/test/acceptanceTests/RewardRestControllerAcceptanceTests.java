package test.acceptanceTests;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.classesWithRestTestsMethod.rewardCalculationApp.CleanRewardDbForTest;
import test.classesWithRestTestsMethod.rewardCalculationApp.EmployeeClassWithRestMethodsForAcceptanceTests;
import test.classesWithRestTestsMethod.rewardCalculationApp.RewardClassWithMethodsForAcceptanceTests;
import test.classesWithRestTestsMethod.rewardCalculationApp.TariffClassWithMethodsForAcceptanceTests;

import java.math.BigDecimal;
import java.util.Objects;

import static org.hamcrest.Matchers.equalTo;

public class RewardRestControllerAcceptanceTests {

    //@BeforeEach
    public void cleanDB(){
        CleanRewardDbForTest.rewardCalculationCleanDb(true,true,true);
    }
    //Test PASSED!
    //@Test
    public void acceptanceTest(){
        TariffClassWithMethodsForAcceptanceTests.createTariff("speech",new BigDecimal("29.29"));
        //Unsuccessful
        EmployeeClassWithRestMethodsForAcceptanceTests.createEmployee
                (null,"Lasamanba",new BigDecimal("3.5")).then().statusCode(200);
        RewardClassWithMethodsForAcceptanceTests.createReward(null,null)
                .then().statusCode(200)
                .body("errors[0].errorCode",equalTo("ERROR_CODE_FOR_REWARD_2"))
                .body("errors[1].errorCode",equalTo("ERROR_CODE_FOR_TARIFF_1"));
        RewardClassWithMethodsForAcceptanceTests.createReward(1L,"null")
                .then().statusCode(200)
                .body("errors[0].errorCode",equalTo("ERROR_CODE_FOR_TARIFF_4"));
        //Successful

        Response response =  EmployeeClassWithRestMethodsForAcceptanceTests.createEmployee
                ("Harbon","Lasamanba",new BigDecimal("3.5"));
        response.then().statusCode(200);
        Long employeeId = response.jsonPath().getLong("employeeDTO.id");

        Response rewardResponse = RewardClassWithMethodsForAcceptanceTests.createReward(employeeId,"speech");
        rewardResponse.then().statusCode(200);
        Long  rewardId = rewardResponse.jsonPath().getLong("rewardDTO.id");

        RewardClassWithMethodsForAcceptanceTests.getReward(rewardId).then().statusCode(200).body("rewardDTO.id",equalTo(rewardId.intValue()))
                .body("rewardDTO.employeeId", equalTo(employeeId.intValue()))
                .body("rewardDTO.jobType",equalTo("SPEECH"))
                .body("rewardDTO.status",equalTo("UNPAID"));
    }

    //Test PASSED!
    //@Test
    public void acceptanceTest2(){
        TariffClassWithMethodsForAcceptanceTests.createTariff("speech",new BigDecimal("23.24")).then().statusCode(200);

        // 1. Создаём сотрудника
        Response employeeResponse = EmployeeClassWithRestMethodsForAcceptanceTests.createEmployee("Иван","Иванов",new BigDecimal("1.2"));
        employeeResponse.then().statusCode(200);
        Long employeeId = employeeResponse.jsonPath().getLong("employeeDTO.id");

        // 2. Создаём награду
        RewardClassWithMethodsForAcceptanceTests.createReward(employeeId,"speech");

        RewardClassWithMethodsForAcceptanceTests.createReward(employeeId,"speech")
                .then().statusCode(200).body("errors[0].errorCode", equalTo("ERROR_CODE_FOR_REWARD_3"));
    }
    //Test PASSED!
    //@Test
    public void acceptanceTest3(){
        // 1. Создаём сотрудника
        Response employeeResponse = EmployeeClassWithRestMethodsForAcceptanceTests.createEmployee("Иван","Иванов",new BigDecimal("1.2"));
        employeeResponse.then().statusCode(200);
        Long employeeId = employeeResponse.jsonPath().getLong("employeeDTO.id");

        // 2. Создаём награду
        RewardClassWithMethodsForAcceptanceTests.createReward(employeeId,"speech");

        RewardClassWithMethodsForAcceptanceTests.createReward(employeeId,"speech")
                .then().statusCode(200).body("errors[0].errorCode", equalTo("ERROR_CODE_FOR_TARIFF_5"));
    }
}
