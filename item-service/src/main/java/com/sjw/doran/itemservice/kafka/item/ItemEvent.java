package com.sjw.doran.itemservice.kafka.item;

import com.sjw.doran.itemservice.kafka.common.OperationType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ItemEvent extends ApplicationEvent {

    private final Long id;
    private final ItemTopicMessage.Payload payload;
    private final OperationType operationType;

    public ItemEvent(Object source, Long id, ItemTopicMessage.Payload payload, OperationType operationType) {
        super(source);
        this.id = id;
        this.payload = payload;
        this.operationType = operationType;
    }
}
