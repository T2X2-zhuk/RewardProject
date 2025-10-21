package test.acceptanceTests;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.DTOS.JobTypeDTO;
import test.classesWithRestTestsMethod.rewardCalculationApp.CleanRewardDbForTest;
import test.classesWithRestTestsMethod.rewardCalculationApp.JobTypeClassWithRestMethodsForAcceptanceTests;
import static org.hamcrest.Matchers.equalTo;
public class JobTypeRestControllerAcceptanceTests {

    //@BeforeEach
    public void cleanDB(){
        CleanRewardDbForTest.rewardCalculationCleanDb(true,true,true,true,true);
    }

    //@Test
    public void acceptanceTest(){
        Response response = JobTypeClassWithRestMethodsForAcceptanceTests.createJobType(JobTypeDTO.builder().jobType("HELP").build());
        Long jobTypeId = response.jsonPath().getLong("jobTypeDTO.id");

        JobTypeClassWithRestMethodsForAcceptanceTests.createJobType(JobTypeDTO.builder().jobType(null).build()).then().statusCode(200)
                .body("errors[0].errorCode",equalTo("ERROR_CODE_FOR_JOB_TYPE_1"));

        JobTypeClassWithRestMethodsForAcceptanceTests.createJobType(JobTypeDTO.builder().jobType("HELP").build())
                .then().body("errors[0].errorCode",equalTo("ERROR_CODE_FOR_JOB_TYPE_4"));

        JobTypeClassWithRestMethodsForAcceptanceTests.getJobType(jobTypeId).then().statusCode(200)
                .body("jobTypeDTO.id",equalTo(jobTypeId.intValue()))
                .body("jobTypeDTO.jobType",equalTo("HELP"));

        JobTypeClassWithRestMethodsForAcceptanceTests.getJobType(jobTypeId+1)
                .then().statusCode(200)
                .body("errors[0].errorCode",equalTo("ERROR_CODE_FOR_JOB_TYPE_2"));
    }
}
