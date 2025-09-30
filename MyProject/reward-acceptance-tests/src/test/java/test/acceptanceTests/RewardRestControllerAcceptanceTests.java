package test.acceptanceTests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import test.classesWithRestTestsMethod.rewardCalculationApp.CleanRewardDbForTest;
import test.classesWithRestTestsMethod.rewardCalculationApp.EmployeeClassWithRestMethodsForAcceptanceTests;
import test.classesWithRestTestsMethod.rewardCalculationApp.RewardClassWithMethodsForAcceptanceTests;
import test.classesWithRestTestsMethod.rewardCalculationApp.TariffClassWithMethodsForAcceptanceTests;

import static org.hamcrest.Matchers.equalTo;

public class RewardRestControllerAcceptanceTests {


    //Test PASSED!
    //@Test
    public void acceptanceTest(){
        CleanRewardDbForTest.rewardCalculationCleanDb(true,true,true);
        TariffClassWithMethodsForAcceptanceTests.createTariff("speech",29.29);
        //Unsuccessful
        EmployeeClassWithRestMethodsForAcceptanceTests.createEmployee
                (null,"Lasamanba",3.5).then().statusCode(200);
        RewardClassWithMethodsForAcceptanceTests.createReward(null,null)
                .then().statusCode(200)
                .body("errors[0].errorCode",equalTo("ERROR_CODE_For_Reward_2"))
                .body("errors[1].errorCode",equalTo("ERROR_CODE_For_Tariff_1"));
        RewardClassWithMethodsForAcceptanceTests.createReward(1L,"null")
                .then().statusCode(200)
                .body("errors[0].errorCode",equalTo("ERROR_CODE_For_Tariff_4"));
        //Successful

        Response response =  EmployeeClassWithRestMethodsForAcceptanceTests.createEmployee
                ("Harbon","Lasamanba",3.5);
        response.then().statusCode(200);
        Long employeeId = response.jsonPath().getLong("employeeDTO.id");

        Response rewardResponse = RewardClassWithMethodsForAcceptanceTests.createReward(employeeId,"speech");
        rewardResponse.then().statusCode(200);
        Long  rewardId = rewardResponse.jsonPath().getLong("rewardDTO.id");

        RewardClassWithMethodsForAcceptanceTests.getReward(rewardId).then().statusCode(200).body("rewardDTO.id",equalTo(rewardId.intValue()))
                .body("rewardDTO.employeeId", equalTo(employeeId.intValue()))
                .body("rewardDTO.jobType",equalTo("speech"));
    }
}
