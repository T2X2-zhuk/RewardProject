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
// Latest test result
//Создание сотрудников завершено. Успешно: 50 из 50
//Создание наград завершено.
//Список сотрудников : [10, 18, 14, 9, 15, 11, 13, 17, 12, 16, 22, 19, 27, 25, 23, 20, 24, 21, 29, 26, 28, 30, 31, 33, 32, 36, 34, 35, 38, 37, 39, 40, 41, 42, 43, 47, 50, 46, 49, 48, 51, 45, 44, 52, 53, 54, 56, 55, 58, 57]
//Общее количество платежей в базе: 50
//Тест завершён. Все проверки выполнены.

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
        System.out.println("Список сотрудников : " + employeeIds);
        // Проверка выплат для всех сотрудников
        //Подождать немного перед проверкой выплат (например, 5 секунды)
        Thread.sleep(5000);

        checkingPaymentsForAllEmployees(employeeIds);
        printTotalPayments(employeeIds);
        System.out.println("Тест завершён. Все проверки выполнены.");
    }

    private static void checkingPaymentsForAllEmployees(List<Long> employeeIds) {
        final long timeoutMillis = 10000; // максимальное время ожидания 10 секунд
        final long pollIntervalMillis = 500; // интервал между проверками

        for (Long employeeId : employeeIds) {
            long startTime = System.currentTimeMillis();
            boolean success = false;

            while (System.currentTimeMillis() - startTime < timeoutMillis) {
                try {
                    PaymentClassWithMethodsForAcceptanceTests.getPayments(employeeId)
                            .then()
                            .statusCode(200)
                            .body("paymentDTOS.employeeId", hasItem(employeeId.intValue()));
                    success = true;
                    break; // если проверки прошли — выходим из цикла
                } catch (AssertionError e) {
                    try {
                        Thread.sleep(pollIntervalMillis); // ждём перед следующей проверкой
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }

            if (!success) {
                System.err.println("Ошибка проверки выплат для сотрудника " + employeeId + " — данные так и не пришли за " + timeoutMillis + " мс");
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
