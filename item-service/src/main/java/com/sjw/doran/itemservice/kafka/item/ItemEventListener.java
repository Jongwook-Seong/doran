package com.sjw.doran.itemservice.kafka.item;

import com.sjw.doran.itemservice.kafka.producer.ItemProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ItemEventListener {

    private final ItemProducer itemProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void transactionalEventListenerAfterCommit(ItemEvent event) {
        System.out.println("ItemEventListener.transactionalEventListenerAfterCommit");
        itemProducer.sendMessage(new ItemTopicMessage(event.getId(), event.getPayload(), event.getOperationType()));
    }
}
