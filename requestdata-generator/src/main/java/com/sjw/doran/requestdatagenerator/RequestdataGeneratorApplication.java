package com.sjw.doran.requestdatagenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RequestdataGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(RequestdataGeneratorApplication.class, args);
    }

}
