package test.acceptanceTests;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.DTOS.JobTypeDTO;
import test.classesWithRestTestsMethod.rewardCalculationApp.*;
import java.math.BigDecimal;
import static org.hamcrest.Matchers.equalTo;

public class RewardRestControllerAcceptanceTests {

    //@BeforeEach
    public void cleanDB(){
        CleanRewardDbForTest.rewardCalculationCleanDb(true,true,true,true,true);
    }
    //Test PASSED!
    //@Test
    public void acceptanceTestWithErrorCreateReward(){
        JobTypeClassWithRestMethodsForAcceptanceTests.createJobType(JobTypeDTO.builder().jobType("Speech").build());
        TariffClassWithMethodsForAcceptanceTests.createTariff("speech",new BigDecimal("29.29"));
        Response response = EmployeeClassWithRestMethodsForAcceptanceTests.createEmployee("Danil","Tarasov",new BigDecimal("3.5"));
        Long employeeId = response.jsonPath().getLong("employeeDTO.id");

        RewardClassWithMethodsForAcceptanceTests.createReward(employeeId,"speech");


        RewardClassWithMethodsForAcceptanceTests.createReward(null,null)
                .then().statusCode(200)
                .body("errors[0].errorCode",equalTo("ERROR_CODE_FOR_REWARD_2"))
                .body("errors[1].errorCode",equalTo("ERROR_CODE_FOR_JOB_TYPE_1"));

        RewardClassWithMethodsForAcceptanceTests.createReward(employeeId + 1200,"Lacrimoza")
                .then().statusCode(200)
                .body("errors[0].errorCode",equalTo("ERROR_CODE_FOR_EMPLOYEE_5"))
                .body("errors[1].errorCode",equalTo("ERROR_CODE_FOR_TARIFF_4"));
        CleanRewardDbForTest.rewardCalculationCleanDb(false,false,true,false,false);

        RewardClassWithMethodsForAcceptanceTests.createReward(employeeId,"Lacrimoza")
                .then().statusCode(200)
                .body("errors[0].errorCode",equalTo("ERROR_CODE_FOR_TARIFF_5"));
    }

    //Test PASSED!
    //@Test
    public void acceptanceTestWithErrorERROR_CODE_FOR_REWARD_3(){
        JobTypeClassWithRestMethodsForAcceptanceTests.createJobType(JobTypeDTO.builder().jobType("Speech").build());
        TariffClassWithMethodsForAcceptanceTests.createTariff("speech",new BigDecimal("29.29"));
        Response response = EmployeeClassWithRestMethodsForAcceptanceTests.createEmployee("Danil","Tarasov",new BigDecimal("3.5"));
        Long employeeId = response.jsonPath().getLong("employeeDTO.id");
        RewardClassWithMethodsForAcceptanceTests.createReward(employeeId,"speech");
        RewardClassWithMethodsForAcceptanceTests.createReward(employeeId,"speech").then().statusCode(200).body("errors[0].errorCode", equalTo("ERROR_CODE_FOR_REWARD_3"));
    }
    //Test PASSED!
    //@Test
    public void acceptanceTestSuccessfulCreate(){
        JobTypeClassWithRestMethodsForAcceptanceTests.createJobType(JobTypeDTO.builder().jobType("Speech").build());
        TariffClassWithMethodsForAcceptanceTests.createTariff("speech",new BigDecimal("29.29"));
        Response response = EmployeeClassWithRestMethodsForAcceptanceTests.createEmployee("Danil","Tarasov",new BigDecimal("3.5"));
        Long employeeId = response.jsonPath().getLong("employeeDTO.id");
        Response response2 = RewardClassWithMethodsForAcceptanceTests.createReward(employeeId,"speech");
        Long rewardId = response2.jsonPath().getLong("rewardDTO.id");

        RewardClassWithMethodsForAcceptanceTests.getReward(rewardId+23)
                        .then().statusCode(200).body("errors[0].errorCode",equalTo("ERROR_CODE_FOR_REWARD_4"));

        RewardClassWithMethodsForAcceptanceTests.getReward(rewardId)
                .then().statusCode(200).body("rewardDTO.employeeId",equalTo(employeeId.intValue()))
                .body("rewardDTO.jobType",equalTo("SPEECH"))
                .body("rewardDTO.status",equalTo("UNPAID"));
    }
}
