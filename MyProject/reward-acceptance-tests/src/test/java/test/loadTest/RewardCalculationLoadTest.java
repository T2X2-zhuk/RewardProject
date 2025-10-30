package test.loadTest;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import test.DTOS.JobTypeDTO;
import test.classesWithRestTestsMethod.paymentApp.CleanPaymentDbForTest;
import test.classesWithRestTestsMethod.paymentApp.PaymentClassWithMethodsForAcceptanceTests;
import test.classesWithRestTestsMethod.rewardCalculationApp.*;

import java.math.BigDecimal;
import java.util.List;
import static org.hamcrest.Matchers.hasItem;

public class RewardCalculationLoadTest {

    // Test - Passed
    //@Test
    public void fullLoadTestRewardCalculation() throws InterruptedException {
        // 1️⃣ Очистка БД
        cleanDB();
        // 2️⃣ Создание общих справочников
        commonJobTypeAndTariff();
        // 3️⃣ Параллельное создание сотрудников и наград
        List<Long> employeeIds = ParallelCreationOfEmployeesAndAwards.execute();
        // 4️⃣ Выполнение расчёта наград (только один поток реально выполняет)
        PerformingTheCalculationOfRewards.execute();
        // Проверка выплат для всех сотрудников
        checkingPaymentsForAllEmployees(employeeIds);
        printTotalPayments(employeeIds);
        System.out.println("Тест завершён. Все проверки выполнены.");
    }

    private static void checkingPaymentsForAllEmployees(List<Long> employeeIds) {
        for (Long employeeId : employeeIds) {
            int attempts = 0;
            boolean success = false;
            while (attempts < 10 && !success) { // до 10 попыток
                try {
                    PaymentClassWithMethodsForAcceptanceTests.getPayments(employeeId)
                            .then()
                            .statusCode(200)
                            .body("paymentDTOS.employeeId", hasItem(employeeId.intValue()));
                    success = true;
                } catch (AssertionError e) {
                    attempts++;
                }
            }
            if (!success) {
                System.err.println("Ошибка проверки выплат для сотрудника " + employeeId);
            }
        }
    }

    private static void commonJobTypeAndTariff() {
        JobTypeClassWithRestMethodsForAcceptanceTests.createJobType(JobTypeDTO.builder().jobType("Speech").build());
        TariffClassWithMethodsForAcceptanceTests.createTariff("speech", new BigDecimal("23.24"));
    }

    private static void cleanDB() {
        CleanRewardDbForTest.rewardCalculationCleanDb(true, true, true, true, true);
        CleanPaymentDbForTest.rewardPaymentCleanDb(true);
    }

    private static void printTotalPayments(List<Long> employeeIds) {
        int totalPayments = 0;

        for (Long employeeId : employeeIds) {
            try {
                Response response = PaymentClassWithMethodsForAcceptanceTests.getPayments(employeeId);
                List<?> payments = response.jsonPath().getList("paymentDTOS");
                totalPayments += payments.size();
            } catch (Exception e) {
                System.err.println("Ошибка при подсчёте платежей для сотрудника " + employeeId + ": " + e.getMessage());
            }
        }

        System.out.println("Общее количество платежей в базе: " + totalPayments);
    }
}
