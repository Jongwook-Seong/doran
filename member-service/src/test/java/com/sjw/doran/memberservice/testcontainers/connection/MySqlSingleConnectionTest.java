package com.sjw.doran.memberservice.testcontainers.connection;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Slf4j
@Testcontainers
public class MySqlSingleConnectionTest {

    @Container
//    MySQLContainer mySQLContainer = new MySQLContainer("mysql:9.2.0");
    static JdbcDatabaseContainer mySQLContainer = new MySQLContainer("mysql:9.2.0")
            .withDatabaseName("test-member")
            .withUsername("doranms")
            .withPassword("doranms1234!");

    @BeforeEach
    void setupContainer() {
        mySQLContainer.start();
    }

    @AfterEach
    void stopContainer() {
        mySQLContainer.stop();
    }

    @Test
    void 커넥션_테스트() {
        log.info("JDBC Driver Instance: {}", mySQLContainer.getJdbcDriverInstance());
        log.info("JDBC URL: {}", mySQLContainer.getJdbcUrl());
        log.info("Mapped Port: {}", mySQLContainer.getMappedPort(3306));
        log.info("Host: {}", mySQLContainer.getHost());
        log.info("Username: {}", mySQLContainer.getUsername());
        log.info("Password: {}", mySQLContainer.getPassword());
    }
}
