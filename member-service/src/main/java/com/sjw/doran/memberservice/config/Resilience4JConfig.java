package com.sjw.doran.memberservice.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Slf4j
@Configuration
public class Resilience4JConfig {

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> globalCustomConfiguration() {

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(10)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(5)
                .build();

        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(4))
                .build();

        RetryConfig retryConfig = RetryConfig.custom()
                .maxAttempts(5)
                .waitDuration(Duration.ofMillis(500))
                .retryExceptions(RuntimeException.class)
                .build();

        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .timeLimiterConfig(timeLimiterConfig)
                .circuitBreakerConfig(circuitBreakerConfig)
                .build());
    }

    @Bean
    public RegistryEventConsumer<Retry> retryRegistryEventConsumer() {
        return new RegistryEventConsumer<Retry>() {
            @Override
            public void onEntryAddedEvent(EntryAddedEvent<Retry> entryAddedEvent) {
                log.info("RegistryEventConsumer<Retry>.onEntryAddedEvent");
                entryAddedEvent.getAddedEntry().getEventPublisher().onEvent(event -> log.info(event.toString()));
            }

            @Override
            public void onEntryRemovedEvent(EntryRemovedEvent<Retry> entryRemoveEvent) {
                log.info("RegistryEventConsumer<Retry>.onEntryRemovedEvent");
            }

            @Override
            public void onEntryReplacedEvent(EntryReplacedEvent<Retry> entryReplacedEvent) {
                log.info("RegistryEventConsumer<Retry>.onEntryReplacedEvent");
            }
        };
    }

    @Bean
    public RegistryEventConsumer<CircuitBreaker> circuitBreakerRegistryEventConsumer() {

        return new RegistryEventConsumer<CircuitBreaker>() {
            @Override
            public void onEntryAddedEvent(EntryAddedEvent<CircuitBreaker> entryAddedEvent) {
                log.info("RegistryEventConsumer<CircuitBreaker>.onEntryAddedEvent");

                CircuitBreaker.EventPublisher eventPublisher = entryAddedEvent.getAddedEntry().getEventPublisher();

                eventPublisher.onEvent(event -> log.info("onEvent {}", event));
                eventPublisher.onSuccess(event -> log.info("onSuccess {}", event));
                eventPublisher.onCallNotPermitted(event -> log.info("onCallNotPermitted {}", event));
                eventPublisher.onError(event -> log.info("onError {}", event));
                eventPublisher.onIgnoredError(event -> log.info("onIgnoredError {}", event));
                eventPublisher.onStateTransition(event -> log.info("onStateTransition {}", event));
                eventPublisher.onSlowCallRateExceeded(event -> log.info("onSlowCallRateExceeded {}", event));
                eventPublisher.onFailureRateExceeded(event -> log.info("onFailureRateExceeded {}", event));
            }

            @Override
            public void onEntryRemovedEvent(EntryRemovedEvent<CircuitBreaker> entryRemoveEvent) {
                log.info("RegistryEventConsumer<CircuitBreaker>.onEntryRemovedEvent");
            }

            @Override
            public void onEntryReplacedEvent(EntryReplacedEvent<CircuitBreaker> entryReplacedEvent) {
                log.info("RegistryEventConsumer<CircuitBreaker>.onEntryReplacedEvent");
            }
        };
    }
}
