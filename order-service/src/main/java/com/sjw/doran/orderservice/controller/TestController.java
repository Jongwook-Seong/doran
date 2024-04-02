package com.sjw.doran.orderservice.controller;

import com.sjw.doran.orderservice.dto.OrderDto;
import com.sjw.doran.orderservice.dto.OrderItemDto;
import com.sjw.doran.orderservice.entity.Order;
import com.sjw.doran.orderservice.entity.OrderItem;
import com.sjw.doran.orderservice.repository.DeliveryRepository;
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

        orderRepository.save(order);
        orderItemRepository.saveAll(orderItemList);
    }

    @GetMapping("/get")
    public Order getOrder(@RequestParam("orderUuid") String orderUuid) {
        Order order = orderRepository.findByOrderUuid(orderUuid).orElseThrow(() -> {
            throw new RuntimeException("Invalid orderUuid"); });
        return order;
    }
}
