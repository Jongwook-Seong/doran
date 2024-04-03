package com.sjw.doran.orderservice.controller;

import com.sjw.doran.orderservice.dto.DeliveryDto;
import com.sjw.doran.orderservice.dto.DeliveryTrackingDto;
import com.sjw.doran.orderservice.dto.OrderDto;
import com.sjw.doran.orderservice.dto.OrderItemDto;
import com.sjw.doran.orderservice.entity.*;
import com.sjw.doran.orderservice.repository.DeliveryRepository;
import com.sjw.doran.orderservice.repository.DeliveryTrackingRepository;
import com.sjw.doran.orderservice.repository.OrderItemRepository;
import com.sjw.doran.orderservice.repository.OrderRepository;
import com.sjw.doran.orderservice.vo.ItemSimpleInfo;
import com.sjw.doran.orderservice.vo.request.OrderCreateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final DeliveryRepository deliveryRepository;
    private final DeliveryTrackingRepository deliveryTrackingRepository;
    private final ModelMapper modelMapper;

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

        Order order = modelMapper.map(orderDto, Order.class);
        List<OrderItem> orderItemList = new ArrayList<>();
        for (OrderItemDto orderItemDto : orderItemDtoList) {
            orderItemList.add(modelMapper.map(orderItemDto, OrderItem.class));
        }
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrder(order);
        }

//        TransceiverInfo transceiverInfo = new TransceiverInfo("kim", "lee", "010-1234-5678");
//        Address address = new Address("incheon", "jangjaero 995 beongil 44", "7-507", "21021");
        TransceiverInfo transceiverInfo = request.getTransceiverInfo();
        Address address = request.getAddress();
        DeliveryDto deliveryDto = DeliveryDto.getInstanceForCreate(transceiverInfo, address);
        Delivery delivery = modelMapper.map(deliveryDto, Delivery.class);

        order.setDelivery(delivery);

        DeliveryTrackingDto deliveryTrackingDto =
                DeliveryTrackingDto.getInstanceForCreate("kim", "010-xxxx-xxxx", "seoul");
        DeliveryTracking deliveryTracking = modelMapper.map(deliveryTrackingDto, DeliveryTracking.class);
        deliveryTracking.setDelivery(delivery);

        orderRepository.save(order);
        orderItemRepository.saveAll(orderItemList);
        deliveryRepository.save(delivery);
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
