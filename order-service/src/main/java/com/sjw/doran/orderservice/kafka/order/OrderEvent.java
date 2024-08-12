package com.sjw.doran.orderservice.kafka.order;

import com.sjw.doran.orderservice.kafka.common.OperationType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderEvent extends ApplicationEvent {

    private final Long id;
    private final OrderTopicMessage.Payload payload;
    private final OperationType operationType;

    public OrderEvent(Object source, Long id, OrderTopicMessage.Payload payload, OperationType operationType) {
        super(source);
        this.id = id;
        this.payload = payload;
        this.operationType = operationType;
    }
}
