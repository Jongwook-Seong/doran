package com.sjw.doran.memberservice.kafka.member;

import com.sjw.doran.memberservice.kafka.common.OperationType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MemberEvent extends ApplicationEvent {

    private final Long id;
    private final MemberTopicMessage.Payload payload;
    private final OperationType operationType;

    public MemberEvent(Object source, Long id, MemberTopicMessage.Payload payload, OperationType operationType) {
        super(source);
        this.id = id;
        this.payload = payload;
        this.operationType = operationType;
    }
}
