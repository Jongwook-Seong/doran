package com.sjw.doran.memberservice.testcontainers;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import org.testcontainers.containers.DockerComposeContainer;

import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Disabled
@SpringBootTest
@Transactional
@ContextConfiguration(initializers = IntegrationTest.IntegrationTestInitializer.class)
public class IntegrationTest {

    private static final DockerComposeContainer<?> DOCKER_COMPOSE = new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
            .withExposedService("mysql", 23306, Wait.forLogMessage(".*ready for connections.*", 1))
            .withExposedService("redis", 26379, Wait.forLogMessage(".*Ready to accept connections.*", 1))
            .withExposedService("kafka", 29092, Wait.forLogMessage(".*started.*", 1));
    private static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>(DockerImageName.parse("mysql:9.2.0"))
            .withDatabaseName("test-member")
            .withUsername("doranms")
            .withPassword("doranms1234!");
    private static final RedisContainer REDIS_CONTAINER = new RedisContainer(RedisContainer.DEFAULT_IMAGE_NAME.withTag("7.4.2"));
    private static final MongoDBContainer MONGO_DB_CONTAINER = new MongoDBContainer(DockerImageName.parse("mongo:8.0"));
    private static final KafkaContainer KAFKA_CONTAINER = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.8.1"));

    @BeforeAll
    public static void setupContainers() {
        MYSQL_CONTAINER.start();
        REDIS_CONTAINER.start();
        MONGO_DB_CONTAINER.start();
        KAFKA_CONTAINER.start();
    }

    @AfterAll
    public static void stopContainers() {
        MYSQL_CONTAINER.stop();
        REDIS_CONTAINER.stop();
        MONGO_DB_CONTAINER.stop();
        KAFKA_CONTAINER.stop();
    }

    static class IntegrationTestInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(org.springframework.context.ConfigurableApplicationContext applicationContext) {
            Map<String, String> properties = new HashMap<>();

            setMysqlProperties(properties);
            setRedisProperties(properties);
            setMongodbProperties(properties);
            setKafkaProperties(properties);

            TestPropertyValues.of(properties).applyTo(applicationContext);
        }
    }

    private static void setMysqlProperties(Map<String, String> properties) {
        properties.put("spring.datasource.url", MYSQL_CONTAINER.getJdbcUrl());
        properties.put("spring.datasource.username", MYSQL_CONTAINER.getUsername());
        properties.put("spring.datasource.password", MYSQL_CONTAINER.getPassword());
    }

    private static void setRedisProperties(Map<String, String> properties) {
        properties.put("spring.redis.host", REDIS_CONTAINER.getHost());
        properties.put("spring.redis.port", REDIS_CONTAINER.getFirstMappedPort().toString());
    }

    private static void setMongodbProperties(Map<String, String> properties) {
        properties.put("spring.data.mongodb.uri", MONGO_DB_CONTAINER.getReplicaSetUrl());
    }

    private static void setKafkaProperties(Map<String, String> properties) {
        properties.put("spring.kafka.bootstrap-servers", KAFKA_CONTAINER.getBootstrapServers());
    }
}
