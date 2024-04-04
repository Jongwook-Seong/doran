package com.sjw.doran.orderservice.service.impl;

import com.sjw.doran.orderservice.dto.DeliveryDto;
import com.sjw.doran.orderservice.dto.DeliveryTrackingDto;
import com.sjw.doran.orderservice.dto.OrderDto;
import com.sjw.doran.orderservice.dto.OrderItemDto;
import com.sjw.doran.orderservice.entity.*;
import com.sjw.doran.orderservice.repository.DeliveryRepository;
import com.sjw.doran.orderservice.repository.DeliveryTrackingRepository;
import com.sjw.doran.orderservice.repository.OrderItemRepository;
import com.sjw.doran.orderservice.repository.OrderRepository;
import com.sjw.doran.orderservice.service.OrderService;
import com.sjw.doran.orderservice.vo.DeliveryTrackingInfo;
import com.sjw.doran.orderservice.vo.ItemSimpleInfo;
import com.sjw.doran.orderservice.vo.OrderItemSimple;
import com.sjw.doran.orderservice.vo.OrderSimple;
import com.sjw.doran.orderservice.vo.request.OrderCreateRequest;
import com.sjw.doran.orderservice.vo.response.DeliveryTrackingResponse;
import com.sjw.doran.orderservice.vo.response.OrderDetailResponse;
import com.sjw.doran.orderservice.vo.response.OrderListResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final DeliveryRepository deliveryRepository;
    private final DeliveryTrackingRepository deliveryTrackingRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void createOrder(String userUuid, OrderCreateRequest request) {
        List<ItemSimpleInfo> itemSimpleInfoList = request.getItemSimpleInfoList();
        TransceiverInfo transceiverInfo = request.getTransceiverInfo();
        Address address = request.getAddress();

        List<OrderItem> orderItemList = createOrderItemList(itemSimpleInfoList);
        Order order = createOrder(userUuid, orderItemList);
        Delivery delivery = createDelivery(transceiverInfo, address);
        order.setDelivery(delivery);

        DeliveryTracking deliveryTracking = createDefaultDeliveryTracking();
        deliveryTracking.setDelivery(delivery);

        orderRepository.save(order);
        orderItemRepository.saveAll(orderItemList);
        deliveryTrackingRepository.save(deliveryTracking);
    }

    @Override
    public void cancelOrder(String userUuid, String orderUuid) {
        orderRepository.updateOrderStatusAsCancel(userUuid, orderUuid);
    }

    @Override
    public OrderListResponse getOrderList(String userUuid) {
        List<Order> orderList = orderRepository.findAllByUserUuid(userUuid);
        List<OrderSimple> orderSimpleList = new ArrayList<>();
        for (Order order : orderList) {
            List<OrderItemSimple> oisList = new ArrayList<>();
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                oisList.add(OrderItemSimple.getInstance(orderItem.getCount(), orderItem.getOrderPrice()));
            }
            orderSimpleList.add(OrderSimple.getInstance(oisList, order.getDelivery().getDeliveryStatus(), order.getOrderDate()));
        }
        return OrderListResponse.getInstance(orderSimpleList);
    }

    @Override
    public OrderDetailResponse getOrderDetail(String userUuid, String orderUuid) {
        Order order = orderRepository.findByUserUuidAndOrderUuid(userUuid, orderUuid).orElseThrow(() -> {
            throw new RuntimeException("Invalid Order"); });

        List<OrderItemSimple> orderItemSimpleList = new ArrayList<>();
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            orderItemSimpleList.add(OrderItemSimple.getInstance(orderItem.getCount(), orderItem.getOrderPrice()));
        }
        Delivery delivery = order.getDelivery();

        return OrderDetailResponse.getInstance(orderItemSimpleList, order.getOrderDate(),
                delivery.getDeliveryStatus(), delivery.getTransceiverInfo(), delivery.getAddress());
    }

    @Override
    public DeliveryTrackingResponse getDeliveryTrackingInfo(String userUuid, String orderUuid) {
        Order order = orderRepository.findByUserUuidAndOrderUuid(userUuid, orderUuid).orElseThrow(() -> {
            throw new RuntimeException("Invalid Order"); });

        Delivery delivery = order.getDelivery();
        List<DeliveryTrackingInfo> deliveryTrackingInfoList = new ArrayList<>();
        List<DeliveryTracking> deliveryTrackings = delivery.getDeliveryTrackings();
        for (DeliveryTracking tracking : deliveryTrackings) {
            deliveryTrackingInfoList.add(DeliveryTrackingInfo
                    .getInstance(tracking.getCourier(), tracking.getContactNumber(), tracking.getPostLocation(), tracking.getPostDateTime()));
        }

        return DeliveryTrackingResponse.getInstance(deliveryTrackingInfoList, delivery.getDeliveryStatus());
    }

    private List<OrderItem> createOrderItemList(List<ItemSimpleInfo> itemSimpleInfoList) {
        List<OrderItemDto> orderItemDtoList = new ArrayList<>();
        itemSimpleInfoList.forEach(info ->
                orderItemDtoList.add(OrderItemDto.getInstanceForCreate(info)));

        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemDtoList.forEach(oiDto ->
                orderItemList.add(modelMapper.map(oiDto, OrderItem.class)));
        return orderItemList;
    }

    private Order createOrder(String userUuid, List<OrderItem> orderItemList) {
        OrderDto orderDto = OrderDto.getInstanceForCreate(userUuid);
        Order order = modelMapper.map(orderDto, Order.class);
        orderItemList.forEach(orderItem -> orderItem.setOrder(order));
        return order;
    }

    private Delivery createDelivery(TransceiverInfo transceiverInfo, Address address) {
        DeliveryDto deliveryDto = DeliveryDto.getInstanceForCreate(transceiverInfo, address);
        return modelMapper.map(deliveryDto, Delivery.class);
    }

    private DeliveryTracking createDefaultDeliveryTracking() {
        DeliveryTrackingDto deliveryTrackingDto =
                DeliveryTrackingDto.getInstanceForCreate("kim", "010-xxxx-xxxx", "seoul");
        return modelMapper.map(deliveryTrackingDto, DeliveryTracking.class);
    }
}
