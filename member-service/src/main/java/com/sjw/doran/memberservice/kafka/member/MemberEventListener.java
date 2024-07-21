package com.sjw.doran.memberservice.kafka.member;

import com.sjw.doran.memberservice.kafka.producer.MemberProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MemberEventListener {

    private final MemberProducer memberProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void transactionalEventListenerAfterCommit(MemberEvent event) {
        System.out.println("MemberEventListener.transactionalEventListenerAfterCommit");
        memberProducer.sendMessage(new MemberTopicMessage(event.getId(), event.getPayload(), event.getOperationType()));
    }
}
