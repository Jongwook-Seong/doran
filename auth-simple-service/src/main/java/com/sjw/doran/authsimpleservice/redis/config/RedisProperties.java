package com.sjw.doran.authsimpleservice.redis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "spring.redis.config")
@Component
public class RedisProperties {

    private int port;
    private String host;
}
