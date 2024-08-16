package com.sjw.doran.orderservice.mongodb.order;

import com.sjw.doran.orderservice.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "orderInfo")
@Data
@AllArgsConstructor
public class OrderDocument {

    @Id
    private Long id;
    private String orderUuid;
    private String userUuid;
    private List<OrderItem> orderItems;
    private Long deliveryId;
    private OrderStatus orderStatus;
    private LocalDateTime orderDate;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {

        private String itemUuid;
        private int orderPrice;
        private int count;
    }
}
