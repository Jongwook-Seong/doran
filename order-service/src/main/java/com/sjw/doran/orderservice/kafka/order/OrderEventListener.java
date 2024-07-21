package com.sjw.doran.orderservice.kafka.order;

import com.sjw.doran.orderservice.kafka.producer.OrderProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final OrderProducer orderProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void transactionalEventListenerAfterCommit(OrderEvent event) {
        System.out.println("OrderEventListener.transactionalEventListenerAfterCommit");
        orderProducer.sendMessage(new OrderTopicMessage(event.getId(), event.getPayload(), event.getOperationType()));
    }
}
