package test.acceptanceTests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import test.classesWithRestTestsMethod.rewardCalculationApp.CleanRewardDbForTest;
import test.classesWithRestTestsMethod.rewardCalculationApp.TariffClassWithMethodsForAcceptanceTests;


import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;
public class TariffRestControllerAcceptanceTests {

    //Test PASSED!
    //@Test
    public void acceptanceTest(){
        CleanRewardDbForTest.rewardCalculationCleanDb(true,true,true);
        //Successful
        Response createResponse = TariffClassWithMethodsForAcceptanceTests.createTariff("speech",new BigDecimal("4.0"));
        createResponse.then().statusCode(200);
        Long tariffId = createResponse.jsonPath().getLong("tariffDTO.id");
        TariffClassWithMethodsForAcceptanceTests.getTariff(tariffId).then().statusCode(200).body("tariffDTO.jobType", equalTo("SPEECH"));

        //Unsuccessful
        TariffClassWithMethodsForAcceptanceTests.createTariff(null,new BigDecimal("0.0")).then().statusCode(200)
                .body("errors[0].errorCode",equalTo("ERROR_CODE_For_Tariff_1"))
                .body("errors[1].errorCode",equalTo("ERROR_CODE_For_Tariff_3"));
        TariffClassWithMethodsForAcceptanceTests.createTariff("speech",new BigDecimal("4.0")).then().statusCode(200)
                .body("errors[0].errorCode",equalTo("ERROR_CODE_For_Tariff_6"));
        TariffClassWithMethodsForAcceptanceTests.getTariff(tariffId + 1L).then().statusCode(200).body("errors[0].errorCode",equalTo("ERROR_CODE_For_Tariff_2"));
    }
}
