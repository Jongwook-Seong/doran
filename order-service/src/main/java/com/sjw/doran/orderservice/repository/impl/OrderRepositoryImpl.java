package com.sjw.doran.orderservice.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sjw.doran.orderservice.entity.DeliveryStatus;
import com.sjw.doran.orderservice.entity.Order;
import com.sjw.doran.orderservice.entity.OrderStatus;
import com.sjw.doran.orderservice.repository.OrderRepositoryCustom;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.sjw.doran.orderservice.entity.QOrder.order;

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
    public List<Order> findAllByUserUuid(String userUuid) {
        List<Order> orderList = queryFactory
                .selectFrom(order)
                .where(order.userUuid.eq(userUuid),
                        order.orderDate.after(getThreeMonthsAgo()))
                .orderBy(order.orderDate.desc())
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

    private LocalDateTime getThreeMonthsAgo() {
        return LocalDateTime.now().minusMonths(3);
    }
}
