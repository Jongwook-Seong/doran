package com.sjw.doran.orderservice.kafka.delivery;

import com.sjw.doran.orderservice.kafka.producer.DeliveryProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class DeliveryEventListener {

    private final DeliveryProducer deliveryProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void transactionalEventListenerAfterCommit(DeliveryEvent event) {
        System.out.println("DeliveryEventListener.transactionalEventListenerAfterCommit");
        deliveryProducer.sendMessage(new DeliveryTopicMessage(event.getId(), event.getPayload(), event.getOperationType()));
    }
}
