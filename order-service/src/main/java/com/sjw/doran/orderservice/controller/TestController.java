package com.sjw.doran.orderservice.controller;

import com.sjw.doran.orderservice.dto.DeliveryDto;
import com.sjw.doran.orderservice.dto.DeliveryTrackingDto;
import com.sjw.doran.orderservice.dto.OrderDto;
import com.sjw.doran.orderservice.dto.OrderItemDto;
import com.sjw.doran.orderservice.entity.*;
import com.sjw.doran.orderservice.mapper.OrderMapper;
import com.sjw.doran.orderservice.repository.DeliveryTrackingRepository;
import com.sjw.doran.orderservice.repository.OrderItemRepository;
import com.sjw.doran.orderservice.repository.OrderRepository;
import com.sjw.doran.orderservice.vo.ItemSimpleInfo;
import com.sjw.doran.orderservice.vo.request.OrderCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final DeliveryTrackingRepository deliveryTrackingRepository;
    private final OrderMapper orderMapper;

    @PostMapping("/new-order")
    public void newOrder(@RequestHeader("userUuid") String userUuid, @RequestBody OrderCreateRequest request) {
        List<ItemSimpleInfo> itemSimpleInfoList = request.getItemSimpleInfoList();
        List<String> itemUuids = new ArrayList<>();
        itemSimpleInfoList.forEach(info -> itemUuids.add(info.getItemUuid()));

        OrderDto orderDto = OrderDto.getInstanceForCreate(userUuid);
        List<OrderItemDto> orderItemDtoList = new ArrayList<>();
        for (ItemSimpleInfo itemSimpleInfo : itemSimpleInfoList) {
            orderItemDtoList.add(OrderItemDto.getInstanceForCreate(itemSimpleInfo));
        }

//        Order order = modelMapper.map(orderDto, Order.class);
        Order order = orderMapper.toOrder(orderDto);
        List<OrderItem> orderItemList = new ArrayList<>();
        for (OrderItemDto orderItemDto : orderItemDtoList) {
//            orderItemList.add(modelMapper.map(orderItemDto, OrderItem.class));
            orderItemList.add(orderMapper.toOrderItem(orderItemDto));
        }
        for (OrderItem orderItem : orderItemList) {
            orderItem.createOrder(order);
        }

        TransceiverInfo transceiverInfo = request.getTransceiverInfo();
        Address address = request.getAddress();
        DeliveryDto deliveryDto = DeliveryDto.getInstanceForCreate(transceiverInfo, address);
//        Delivery delivery = modelMapper.map(deliveryDto, Delivery.class);
        Delivery delivery = orderMapper.toDelivery(deliveryDto);

        order.createDelivery(delivery);

        DeliveryTrackingDto deliveryTrackingDto =
                DeliveryTrackingDto.getInstanceForCreate("kim", "010-xxxx-xxxx", "seoul");
//        DeliveryTracking deliveryTracking = modelMapper.map(deliveryTrackingDto, DeliveryTracking.class);
//        deliveryTracking.setDelivery(delivery);
        DeliveryTracking deliveryTracking = orderMapper.toDeliveryTracking(deliveryTrackingDto, delivery);

        orderRepository.save(order);
        orderItemRepository.saveAll(orderItemList);
        deliveryTrackingRepository.save(deliveryTracking);
    }

    @GetMapping("/get-order")
    public Order getOrder(@RequestParam("orderUuid") String orderUuid) {
        Order order = orderRepository.findByOrderUuid(orderUuid).orElseThrow(() -> {
            throw new RuntimeException("Invalid orderUuid"); });
        return order;
    }

    @GetMapping("/get-delivery")
    public Delivery getDelivery(@RequestParam("orderUuid") String orderUuid) {
        Order order = orderRepository.findByOrderUuid(orderUuid).orElseThrow(() -> {
            throw new RuntimeException("Invalid orderUuid"); });
        Delivery delivery = order.getDelivery();

//        Delivery delivery = deliveryRepository.findByOrder(order);

        return delivery;
    }
}
