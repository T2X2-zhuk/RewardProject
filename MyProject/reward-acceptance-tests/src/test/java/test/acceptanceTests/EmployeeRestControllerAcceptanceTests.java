package test.acceptanceTests;

import io.restassured.response.Response;
import test.classesWithRestTestsMethod.rewardCalculationApp.CleanRewardDbForTest;
import test.classesWithRestTestsMethod.rewardCalculationApp.EmployeeClassWithRestMethodsForAcceptanceTests;

import static org.hamcrest.Matchers.equalTo;

public class EmployeeRestControllerAcceptanceTests {


    //Test PASSED!
    //@Test
    public void acceptanceTest(){
        CleanRewardDbForTest.rewardCalculationCleanDb(true,true,true);
        //Unsuccessful
        EmployeeClassWithRestMethodsForAcceptanceTests.createEmployee
                (null,null,0.0).
                then().statusCode(200).body("errors[0].errorCode" ,
                        equalTo("ERROR_CODE_For_Employee_1"))
                .body("errors[1].errorCode",
                        equalTo("ERROR_CODE_For_Employee_2"))
                .body("errors[2].errorCode",equalTo("ERROR_CODE_For_Employee_3"));

        EmployeeClassWithRestMethodsForAcceptanceTests.getEmployee(2L).then().statusCode(200).body("errors[0].errorCode",equalTo("ERROR_CODE_For_Employee_5"));
        //Successful
        Response response = EmployeeClassWithRestMethodsForAcceptanceTests.createEmployee
                ("Harbon","Lasamanba",3.5);
        response.then().statusCode(200);
        Long employeeId = response.jsonPath().getLong("employeeDTO.id");

        EmployeeClassWithRestMethodsForAcceptanceTests.getEmployee(employeeId).then().statusCode(200).body("employeeDTO.firstName",equalTo("Harbon"))
                .body("employeeDTO.lastName",equalTo("Lasamanba"))
                .body("employeeDTO.bonusCoefficient",equalTo(3.5F));
    }
}
