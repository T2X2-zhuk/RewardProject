package test.acceptanceTests;

import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.classesWithRestTestsMethod.rewardCalculationApp.CleanRewardDbForTest;
import test.classesWithRestTestsMethod.rewardCalculationApp.EmployeeClassWithRestMethodsForAcceptanceTests;

import java.math.BigDecimal;
import static org.hamcrest.Matchers.equalTo;

public class EmployeeRestControllerAcceptanceTests {

    //@BeforeEach
    public void cleanDB(){
        CleanRewardDbForTest.rewardCalculationCleanDb(true,true,true);
    }
    //Test PASSED!
    //@Test
    public void acceptanceTest(){
       //Unsuccessful
        EmployeeClassWithRestMethodsForAcceptanceTests.createEmployee
                (null,null,new BigDecimal("0.0")).
                then().statusCode(200).body("errors[0].errorCode" ,
                        equalTo("ERROR_CODE_FOR_EMPLOYEE_1"))
                .body("errors[1].errorCode",
                        equalTo("ERROR_CODE_FOR_EMPLOYEE_2"))
                .body("errors[2].errorCode",equalTo("ERROR_CODE_FOR_EMPLOYEE_3"));

        EmployeeClassWithRestMethodsForAcceptanceTests.getEmployee(2L).then().statusCode(200).body("errors[0].errorCode",equalTo("ERROR_CODE_FOR_EMPLOYEE_5"));
        //Successful
        Response response = EmployeeClassWithRestMethodsForAcceptanceTests.createEmployee
                ("Harbon","Lasamanba",new BigDecimal("3.5"));
        response.then().statusCode(200);
        Long employeeId = response.jsonPath().getLong("employeeDTO.id");

        EmployeeClassWithRestMethodsForAcceptanceTests.getEmployee(employeeId).then().statusCode(200).body("employeeDTO.firstName",equalTo("Harbon"))
                .body("employeeDTO.lastName",equalTo("Lasamanba"))
                .body("employeeDTO.bonusCoefficient",equalTo(3.5F));
    }
}
