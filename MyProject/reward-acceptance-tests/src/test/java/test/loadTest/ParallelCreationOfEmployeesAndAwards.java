package test.loadTest;

import io.restassured.response.Response;
import test.classesWithRestTestsMethod.rewardCalculationApp.EmployeeClassWithRestMethodsForAcceptanceTests;
import test.classesWithRestTestsMethod.rewardCalculationApp.RewardClassWithMethodsForAcceptanceTests;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ParallelCreationOfEmployeesAndAwards {

    private static final int THREAD_COUNT = 10; // количество потоков
    private static final int TASK_COUNT = 50; // сколько задач (пользователей)

    public static List<Long> execute() throws InterruptedException {
        ExecutorService dataExecutor = Executors.newFixedThreadPool(THREAD_COUNT);
        List<Future<Long>> employeeFutures = new ArrayList<>();

        // 1️⃣ Параллельное создание сотрудников
        for (int i = 0; i < TASK_COUNT; i++) {
            final int taskId = i;
            employeeFutures.add(dataExecutor.submit(() -> createEmployee(taskId)));
        }

        dataExecutor.shutdown();
        dataExecutor.awaitTermination(10, TimeUnit.MINUTES);

        List<Long> employeeIds = new ArrayList<>();
        for (Future<Long> f : employeeFutures) {
            try {
                Long id = f.get();
                if (id != null) {
                    employeeIds.add(id);
                }
            } catch (Exception ignored) {}
        }

        System.out.println("Создание сотрудников завершено. Успешно: " + employeeIds.size() + " из " + TASK_COUNT);

        // 2️⃣ Параллельное создание наград для сотрудников
        ExecutorService rewardExecutor = Executors.newFixedThreadPool(THREAD_COUNT);
        List<Future<Void>> rewardFutures = new ArrayList<>();
        for (Long employeeId : employeeIds) {
            rewardFutures.add(rewardExecutor.submit(() -> {
                createReward(employeeId);
                return null;
            }));
        }

        rewardExecutor.shutdown();
        rewardExecutor.awaitTermination(10, TimeUnit.MINUTES);

        System.out.println("Создание наград завершено.");

        return employeeIds;
    }

    private static Long createEmployee(int taskId) {
        try {
            BigDecimal bonusCoefficient = new BigDecimal("1.0")
                    .add(BigDecimal.valueOf(taskId).multiply(new BigDecimal("0.1")));

            String uniqueSuffix = "-" + System.nanoTime();
            String firstName = "Emp" + taskId + uniqueSuffix;
            String lastName = "User" + taskId + uniqueSuffix;

            Response employee = EmployeeClassWithRestMethodsForAcceptanceTests.createEmployee(
                    firstName, lastName, bonusCoefficient
            );
            Long employeeId = employee.jsonPath().getLong("employeeDTO.id");

            // Проверяем, что сотрудник появился в базе (5 попыток по 200 мс)
            boolean employeeExists = false;
            for (int attempt = 0; attempt < 5; attempt++) {
                Response check = EmployeeClassWithRestMethodsForAcceptanceTests.getEmployee(employeeId);
                if (check.statusCode() == 200) {
                    employeeExists = true;
                    break;
                }
                Thread.sleep(200);
            }

            if (!employeeExists) {
                System.err.println("Сотрудник " + employeeId + " не появился в базе после ожидания");
                return null;
            }

            return employeeId;
        } catch (Exception e) {
            System.err.println("Ошибка создания сотрудника в потоке " + taskId + ": " + e.getMessage());
            return null;
        }
    }

    private static void createReward(Long employeeId) {
        try {
            Response reward = RewardClassWithMethodsForAcceptanceTests.createReward(employeeId, "speech");
            if (reward.statusCode() != 200) {
                System.err.println("Ошибка создания награды для сотрудника " + employeeId);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при создании награды для сотрудника " + employeeId + ": " + e.getMessage());
        }
    }
}
