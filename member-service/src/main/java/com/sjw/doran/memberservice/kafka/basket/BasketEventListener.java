package com.sjw.doran.memberservice.kafka.basket;

import com.sjw.doran.memberservice.kafka.producer.BasketProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class BasketEventListener {

    private final BasketProducer basketProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void transactionalEventListenerAfterCommit(BasketEvent event) {
        System.out.println("BasketEventListener.transactionalEventListenerAfterCommit");
        basketProducer.sendMessage(new BasketTopicMessage(event.getId(), event.getPayload(), event.getOperationType()));
    }
}
