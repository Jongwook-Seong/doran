package com.sjw.doran.orderservice.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sjw.doran.orderservice.entity.*;
import com.sjw.doran.orderservice.repository.OrderRepositoryCustom;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.sjw.doran.orderservice.entity.QDelivery.delivery;
import static com.sjw.doran.orderservice.entity.QOrder.order;
import static com.sjw.doran.orderservice.entity.QOrderItem.orderItem;

public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<Order> findByOrderUuid(String orderUuid) {
        Order findOrder = queryFactory
                .selectFrom(order)
                .where(order.orderUuid.eq(orderUuid))
                .fetchOne();
        return Optional.ofNullable(findOrder);
    }

    @Override
    public Optional<Order> findByUserUuidAndOrderUuid(String userUuid, String orderUuid) {
        Order findOrder = queryFactory
                .selectFrom(order)
                .where(order.userUuid.eq(userUuid),
                        order.orderUuid.eq(orderUuid))
                .fetchOne();
        return Optional.ofNullable(findOrder);
    }

    @Override
    public Optional<Order> findOrderWithItemsAndDeliveryByUserUuidAndOrderUuid(String userUuid, String orderUuid) {
        Order findOrder = queryFactory
                .selectFrom(order)
                .join(order.orderItems, orderItem)
                .fetchJoin()
                .join(order.delivery, delivery)
                .fetchJoin()
                .where(order.userUuid.eq(userUuid),
                        order.orderUuid.eq(orderUuid))
                .distinct()
                .fetchOne();
        return Optional.ofNullable(findOrder);
    }

    @Override
    public Optional<Order> findOrderWithDeliveryByUserUuidAndOrderUuid(String userUuid, String orderUuid) {
        Order findOrder = queryFactory
                .selectFrom(order)
                .join(order.delivery, delivery)
                .fetchJoin()
                .where(order.userUuid.eq(userUuid),
                        order.orderUuid.eq(orderUuid))
                .fetchOne();
        return Optional.ofNullable(findOrder);
    }

    @Override
    public List<Order> findOrdersWithItemsAndDeliveryByUserUuid(String userUuid) {
        List<Order> orderList = queryFactory
                .selectFrom(order)
                .join(order.orderItems, orderItem)
                .fetchJoin()
                .join(order.delivery, delivery)
                .fetchJoin()
                .where(order.userUuid.eq(userUuid),
                        order.orderDate.after(getThreeMonthsAgo()))
                .orderBy(order.orderDate.desc())
                .distinct()
                .fetch();
        return orderList;
    }

    @Override
    @Transactional
    public void updateOrderStatusAsCancel(String userUuid, String orderUuid) {
        Order toCancelOrder = queryFactory
                .selectFrom(order)
                .where(order.userUuid.eq(userUuid),
                        order.orderUuid.eq(orderUuid))
                .fetchOne();

        if (toCancelOrder.getDelivery().getDeliveryStatus() != DeliveryStatus.READY) return;
        toCancelOrder.setOrderStatus(OrderStatus.CANCEL);
    }

    @Override
    @Transactional
    public Delivery updateDeliveryStatus(String orderUuid, DeliveryStatus deliveryStatus) {
        Order toUpdateStatusOrder = queryFactory
                .selectFrom(order)
                .where(order.orderUuid.eq(orderUuid))
                .fetchOne();

        Delivery delivery = toUpdateStatusOrder.getDelivery();
        if (delivery.getDeliveryStatus() != deliveryStatus)
            delivery.setDeliveryStatus(deliveryStatus);
        return delivery;
    }

    private LocalDateTime getThreeMonthsAgo() {
        return LocalDateTime.now().minusMonths(3);
    }
}
