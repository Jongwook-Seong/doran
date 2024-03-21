package com.sjw.doran.authservice.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter @Setter
@ConfigurationProperties(prefix = "spring.data.redis")
@Component
public class RedisProperties {

    private int port;
    private String host;

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }
}
