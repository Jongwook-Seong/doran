package com.sjw.doran.authservice.redis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "spring.data.redis")
@Component
public class RedisProperties {

    private String host;
    private int port;
}
