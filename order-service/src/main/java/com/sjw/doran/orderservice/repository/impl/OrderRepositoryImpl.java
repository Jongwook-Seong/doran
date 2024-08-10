package com.sjw.doran.orderservice.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sjw.doran.orderservice.entity.*;
import com.sjw.doran.orderservice.repository.OrderRepositoryCustom;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.sjw.doran.orderservice.entity.QDelivery.delivery;
import static com.sjw.doran.orderservice.entity.QOrder.order;
import static com.sjw.doran.orderservice.entity.QOrderItem.orderItem;

public class OrderRepositoryImpl implements OrderRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
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
        EntityGraph<Order> graph = entityManager.createEntityGraph(Order.class);
        graph.addAttributeNodes("orderItems", "delivery");

        TypedQuery<Order> query = entityManager.createQuery(
                "SELECT DISTINCT o FROM Order o " +
                        "WHERE o.userUuid = :userUuid AND o.orderUuid = :orderUuid", Order.class);
        query.setParameter("userUuid", userUuid);
        query.setParameter("orderUuid", orderUuid);
        query.setHint("jakarta.persistence.loadgraph", graph);
        query.setHint("org.hibernate.readOnly", true);

        Order findOrder = query.getSingleResult();
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
        EntityGraph<Order> graph = entityManager.createEntityGraph(Order.class);
        graph.addAttributeNodes("orderItems", "delivery");

        TypedQuery<Order> query = entityManager.createQuery(
                "SELECT DISTINCT o FROM Order o " +
                        "WHERE o.userUuid = :userUuid AND o.orderDate > :date " +
                        "ORDER BY o.orderDate DESC", Order.class);
        query.setParameter("userUuid", userUuid);
        query.setParameter("date", getThreeMonthsAgo());
        query.setHint("jakarta.persistence.loadgraph", graph);
        query.setHint("org.hibernate.readOnly", true);

        List<Order> orderList = query.getResultList();
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
        toCancelOrder.updateOrderStatus(OrderStatus.CANCEL);
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
            delivery.updateDeliveryStatus(deliveryStatus);
        return delivery;
    }

    private LocalDateTime getThreeMonthsAgo() {
        return LocalDateTime.now().minusMonths(3);
    }
}
