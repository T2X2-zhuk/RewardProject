package rewardCalculationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import rewardCalculation.RewardCalculationApplication;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(classes = RewardCalculationApplication.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Testcontainers
class RewardCalculationServiceImplTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.36")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    // Заменяем устаревший MockServerContainer на GenericContainer
    @Container
    static GenericContainer<?> mockPaymentService = new GenericContainer<>("mockserver/mockserver:5.15.0")
            .withExposedPorts(1080);

    @BeforeEach
    public void cleanDbAndPrepareMock() {
        // Очищаем базу через CleanRewardDbController
        given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .body("{\"cleanEmployee\":true,\"cleanReward\":true,\"cleanTariff\":true}")
                .when()
                .post("/api/test/rewardDb/cleanDb")
                .then()
                .statusCode(200);

        // Настройка MockServer для /reward/payment/payReward
        MockServerClient mockClient = new MockServerClient(
                mockPaymentService.getHost(),
                mockPaymentService.getMappedPort(1080)
        );

        mockClient.when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/reward/payment/payReward")
        ).respond(
                HttpResponse.response()
                        .withStatusCode(200)
                        .withBody("{\"status\":\"SUCCESS\"}")
        );
    }

    @Test
    public void rewardCalculationExecute_shouldReturnNonEmptyPaymentList() {
        // Выполняем расчет наград и проверяем, что paymentDTOS не пустой
        given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .when()
                .post("/reward/calculation/rewardCalculationExecute")
                .then()
                .statusCode(200)
                .body("paymentDTOS", not(empty()));
    }
}