package test.acceptanceTests;

import io.restassured.response.Response;
import test.DTOS.JobTypeDTO;
import test.classesWithRestTestsMethod.rewardCalculationApp.CleanRewardDbForTest;
import test.classesWithRestTestsMethod.rewardCalculationApp.JobTypeClassWithRestMethodsForAcceptanceTests;
import test.classesWithRestTestsMethod.rewardCalculationApp.TariffClassWithMethodsForAcceptanceTests;
import java.math.BigDecimal;
import static org.hamcrest.Matchers.equalTo;

public class TariffRestControllerAcceptanceTests {

    //@BeforeEach
    public void cleanDB(){
        CleanRewardDbForTest.rewardCalculationCleanDb(true,true,true,true,true);
    }
    //Test PASSED!
    //@Test
    public void acceptanceTest(){
        JobTypeClassWithRestMethodsForAcceptanceTests.createJobType(JobTypeDTO.builder().jobType("Speech").build());
        //Successful
        Response createResponse = TariffClassWithMethodsForAcceptanceTests.createTariff("speech", new BigDecimal("4.0"));
        createResponse.then().statusCode(200);
        Long tariffId = createResponse.jsonPath().getLong("tariffDTO.id");
        TariffClassWithMethodsForAcceptanceTests.getTariff(tariffId).then().statusCode(200).body("tariffDTO.jobType", equalTo("SPEECH"));

        //Unsuccessful
        TariffClassWithMethodsForAcceptanceTests.createTariff(null,new BigDecimal("0.0")).then().statusCode(200)
                .body("errors[0].errorCode",equalTo("ERROR_CODE_FOR_JOB_TYPE_1"))
                .body("errors[1].errorCode",equalTo("ERROR_CODE_FOR_TARIFF_3"));

        TariffClassWithMethodsForAcceptanceTests.createTariff("Lerrors",new BigDecimal("4.0")).then().statusCode(200)
                .body("errors[0].errorCode",equalTo("ERROR_CODE_FOR_JOB_TYPE_3"));

        TariffClassWithMethodsForAcceptanceTests.createTariff("speech", new BigDecimal("4.0")).then().statusCode(200)
                .body("errors[0].errorCode",equalTo("ERROR_CODE_FOR_TARIFF_6"));

        TariffClassWithMethodsForAcceptanceTests.getTariff(tariffId + 1L).then().statusCode(200).body("errors[0].errorCode",equalTo("ERROR_CODE_FOR_TARIFF_2"));
    }
    //@Test
    public void successfulAcceptanceTest(){
        JobTypeClassWithRestMethodsForAcceptanceTests.createJobType(JobTypeDTO.builder().jobType("Speech").build());
        //Successful
        Response createResponse = TariffClassWithMethodsForAcceptanceTests.createTariff("speech", new BigDecimal("4.0"));
        createResponse.then().statusCode(200);
        Long tariffId = createResponse.jsonPath().getLong("tariffDTO.id");
        TariffClassWithMethodsForAcceptanceTests.getTariff(tariffId).then().statusCode(200).body("tariffDTO.jobType", equalTo("SPEECH"))
                .body("tariffDTO.amount", equalTo(4.0F));
    }
}
