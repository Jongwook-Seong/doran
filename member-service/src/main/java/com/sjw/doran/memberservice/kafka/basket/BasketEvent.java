package com.sjw.doran.memberservice.kafka.basket;

import com.sjw.doran.memberservice.kafka.common.BasketOperationType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BasketEvent extends ApplicationEvent {

    private final Long id;
    private final BasketTopicMessage.Payload payload;
    private final BasketOperationType operationType;

    public BasketEvent(Object source, Long id, BasketTopicMessage.Payload payload, BasketOperationType operationType) {
        super(source);
        this.id = id;
        this.payload = payload;
        this.operationType = operationType;
    }
}
