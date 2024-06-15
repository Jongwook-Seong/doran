package com.sjw.doran.apigatewayservice;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import io.github.resilience4j.retry.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class ApigatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApigatewayServiceApplication.class, args);
    }

//    @Bean
//    public RegistryEventConsumer<Retry> retryRegistryEventConsumer() {
//
//        return new RegistryEventConsumer<Retry>() {
//            @Override
//            public void onEntryAddedEvent(EntryAddedEvent<Retry> entryAddedEvent) {
//                log.info("RegistryEventConsumer<Retry>.onEntryAddedEvent");
//                entryAddedEvent.getAddedEntry().getEventPublisher().onEvent(event -> log.info(event.toString()));
//
//            }
//
//            @Override
//            public void onEntryRemovedEvent(EntryRemovedEvent<Retry> entryRemoveEvent) {
//                log.info("RegistryEventConsumer<Retry>.onEntryRemovedEvent");
//            }
//
//            @Override
//            public void onEntryReplacedEvent(EntryReplacedEvent<Retry> entryReplacedEvent) {
//                log.info("RegistryEventConsumer<Retry>.onEntryReplacedEvent");
//            }
//        };
//    }
//
//    @Bean
//    public RegistryEventConsumer<CircuitBreaker> circuitBreakerRegistryEventConsumer() {
//
//        return new RegistryEventConsumer<CircuitBreaker>() {
//            @Override
//            public void onEntryAddedEvent(EntryAddedEvent<CircuitBreaker> entryAddedEvent) {
//                log.info("RegistryEventConsumer<CircuitBreaker>.onEntryAddedEvent");
//
//                CircuitBreaker.EventPublisher eventPublisher = entryAddedEvent.getAddedEntry().getEventPublisher();
//
//                eventPublisher.onEvent(event -> log.info("onEvent {}", event));
//                eventPublisher.onSuccess(event -> log.info("onSuccess {}", event));
//                eventPublisher.onCallNotPermitted(event -> log.info("onCallNotPermitted {}", event));
//                eventPublisher.onError(event -> log.info("onError {}", event));
//                eventPublisher.onIgnoredError(event -> log.info("onIgnoredError {}", event));
//                eventPublisher.onStateTransition(event -> log.info("onStateTransition {}", event));
//                eventPublisher.onSlowCallRateExceeded(event -> log.info("onSlowCallRateExceeded {}", event));
//                eventPublisher.onFailureRateExceeded(event -> log.info("onFailureRateExceeded {}", event));
//            }
//
//            @Override
//            public void onEntryRemovedEvent(EntryRemovedEvent<CircuitBreaker> entryRemoveEvent) {
//                log.info("RegistryEventConsumer<CircuitBreaker>.onEntryRemovedEvent");
//            }
//
//            @Override
//            public void onEntryReplacedEvent(EntryReplacedEvent<CircuitBreaker> entryReplacedEvent) {
//                log.info("RegistryEventConsumer<CircuitBreaker>.onEntryReplacedEvent");
//            }
//        };
//    }
}
