package com.sjw.doran.orderservice.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sjw.doran.orderservice.entity.Order;
import com.sjw.doran.orderservice.entity.OrderItem;
import com.sjw.doran.orderservice.kafka.common.OperationType;
import com.sjw.doran.orderservice.kafka.common.Topic;
import com.sjw.doran.orderservice.kafka.order.OrderTopicMessage;
import com.sjw.doran.orderservice.mapper.CustomObjectMapper;
import com.sjw.doran.orderservice.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final CustomObjectMapper objectMapper = new CustomObjectMapper();
    private final OrderMapper orderMapper;

    public void sendMessage(OrderTopicMessage message) {
        try {
            kafkaTemplate.send(Topic.ORDER_TOPIC, String.valueOf(message.getId()), objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendCreateMessage(Order order, List<OrderItem> orderItems, Long deliveryId) {
        OrderTopicMessage message = orderMapper.toOrderTopicMessage(order, orderItems, deliveryId, OperationType.CREATE);
        sendMessage(message);
    }

    /** 현재 주문 서비스에서 기존 주문에 대한 수정 API는 없음 **/
    public void sendUpdateMessage(Order order, List<OrderItem> orderItems, Long deliveryId) {
        OrderTopicMessage message = orderMapper.toOrderTopicMessage(order, orderItems, deliveryId, OperationType.UPDATE);
        sendMessage(message);
    }

    public void sendDeleteMessage(Long orderId) {
        // TODO: orderId를 파라미터로 어떻게 전달할지 해결 필요
        OrderTopicMessage message = orderMapper.toOrderTopicMessage(null, null, null, OperationType.DELETE);
        sendMessage(message);
    }
}
