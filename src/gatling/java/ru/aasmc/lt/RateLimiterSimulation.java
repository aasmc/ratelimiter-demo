package ru.aasmc.lt;


import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

/**
 * Тестируем, что за 3 минуты, один пользователь может отправить не более
 * 90 GET-запросов (1 запрос в 2 секунды). Нагрузка неравномерная:
 * 1. Начинаем с 5 запросов в секунду в течение 1 минуты
 * 2. Увеличиваем нагрузку до 50 запросов в секунду в течение 1 минуты
 * 3. Поддерживаем нагрузку в 100 запросов в секунду в течение 1 минуты
 *
 * Сценариев запуска нагрузки два - по одному на инстанс сервиса. Они выполняются параллельно.
 * Независимо от количества инстансов, общее количество успешных запросов пользователя на панели
 * мониторинга после отработки скрипта должно быть не более 90.
 */
public class RateLimiterSimulation extends Simulation {


    private static final HttpProtocolBuilder PROTOCOL_BUILDER = setupProtocolForSimulation("9091");
    private static final HttpProtocolBuilder PROTOCOL_BUILDER_2 = setupProtocolForSimulation("9095");
    private static final ScenarioBuilder CREATE_SCENARIO_BUILDER = createScenarioBuilder("Load Test Get Items 1");
    private static final ScenarioBuilder CREATE_SCENARIO_BUILDER_2 = createScenarioBuilder("Load Test Get Items 2");


    private static HttpProtocolBuilder setupProtocolForSimulation(String port) {
        return http.baseUrl("http://localhost:" + port)
                .maxConnectionsPerHost(1)
                .userAgentHeader("Gatling/Load Test");
    }


    private static ScenarioBuilder createScenarioBuilder(String name) {
        return CoreDsl.scenario(name)
                .exec(http("get-item-response").get("/items/Alex")
                        .check(status().is(200)));
    }

    public RateLimiterSimulation() {
        setUp(CREATE_SCENARIO_BUILDER
                        .injectOpen(
                                constantUsersPerSec(1).during(Duration.ofSeconds(180))
                        ).protocols(PROTOCOL_BUILDER)
        ).assertions(
                global().successfulRequests().count().lte(90L)
        );
    }

}
