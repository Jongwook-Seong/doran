package com.sjw.doran.orderservice.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sjw.doran.orderservice.entity.Order;
import com.sjw.doran.orderservice.entity.OrderStatus;
import com.sjw.doran.orderservice.repository.OrderRepositoryCustom;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sjw.doran.orderservice.entity.QOrder.order;

public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private JPAQueryFactory queryFactory;
//    private EntityManager entityManager;

    public OrderRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
//        this.entityManager = entityManager;
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
    @Transactional
    public void updateOrderStatusAsCancel(String userUuid, String orderUuid) {
        Order toCancelOrder = queryFactory
                .selectFrom(order)
                .where(order.userUuid.eq(userUuid),
                        order.orderUuid.eq(orderUuid))
                .fetchOne();

        toCancelOrder.setOrderStatus(OrderStatus.CANCEL);
    }
}
