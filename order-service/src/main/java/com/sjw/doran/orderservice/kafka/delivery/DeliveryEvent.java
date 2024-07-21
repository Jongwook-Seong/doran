package com.sjw.doran.orderservice.kafka.delivery;

import com.sjw.doran.orderservice.kafka.common.OperationType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeliveryEvent extends ApplicationEvent {

    private final Long id;
    private final DeliveryTopicMessage.Payload payload;
    private final OperationType operationType;

    public DeliveryEvent(Object source, Long id, DeliveryTopicMessage.Payload payload, OperationType operationType) {
        super(source);
        this.id = id;
        this.payload = payload;
        this.operationType = operationType;
    }
}
