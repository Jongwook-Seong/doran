package com.sjw.doran.orderservice.service.impl;

import com.sjw.doran.orderservice.client.ResilientItemServiceClient;
import com.sjw.doran.orderservice.dto.DeliveryDto;
import com.sjw.doran.orderservice.dto.DeliveryTrackingDto;
import com.sjw.doran.orderservice.dto.OrderDto;
import com.sjw.doran.orderservice.dto.OrderItemDto;
import com.sjw.doran.orderservice.entity.*;
import com.sjw.doran.orderservice.exception.RecordException;
import com.sjw.doran.orderservice.exception.RetryException;
import com.sjw.doran.orderservice.kafka.common.OperationType;
import com.sjw.doran.orderservice.kafka.delivery.DeliveryEvent;
import com.sjw.doran.orderservice.kafka.delivery.DeliveryTopicMessage;
import com.sjw.doran.orderservice.kafka.order.OrderEvent;
import com.sjw.doran.orderservice.kafka.order.OrderTopicMessage;
import com.sjw.doran.orderservice.mapper.DeliveryMapper;
import com.sjw.doran.orderservice.mapper.DeliveryTrackingMapper;
import com.sjw.doran.orderservice.mapper.OrderMapper;
import com.sjw.doran.orderservice.mongodb.DeliveryDocument;
import com.sjw.doran.orderservice.mongodb.DeliveryDocumentRepository;
import com.sjw.doran.orderservice.repository.DeliveryTrackingRepository;
import com.sjw.doran.orderservice.repository.OrderItemRepository;
import com.sjw.doran.orderservice.repository.OrderRepository;
import com.sjw.doran.orderservice.service.OrderService;
import com.sjw.doran.orderservice.util.MessageUtil;
import com.sjw.doran.orderservice.vo.DeliveryTrackingInfo;
import com.sjw.doran.orderservice.vo.ItemSimpleInfo;
import com.sjw.doran.orderservice.vo.OrderItemSimple;
import com.sjw.doran.orderservice.vo.OrderSimple;
import com.sjw.doran.orderservice.vo.request.DeliveryStatusPostRequest;
import com.sjw.doran.orderservice.vo.request.OrderCreateRequest;
import com.sjw.doran.orderservice.vo.response.DeliveryTrackingResponse;
import com.sjw.doran.orderservice.vo.response.ItemSimpleWithoutPriceResponse;
import com.sjw.doran.orderservice.vo.response.OrderDetailResponse;
import com.sjw.doran.orderservice.vo.response.OrderListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final DeliveryTrackingRepository deliveryTrackingRepository;
    private final DeliveryDocumentRepository deliveryDocumentRepository;
//    private final ItemServiceClient itemServiceClient;
    private final ResilientItemServiceClient resilientItemServiceClient;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final OrderMapper orderMapper;
    private final DeliveryMapper deliveryMapper;
    private final DeliveryTrackingMapper deliveryTrackingMapper;
    private final MessageUtil messageUtil;

    @Override
    @Transactional
    public void createOrder(String userUuid, OrderCreateRequest request) throws InterruptedException {
        List<ItemSimpleInfo> itemSimpleInfoList = request.getItemSimpleInfoList();
        TransceiverInfo transceiverInfo = request.getTransceiverInfo();
        Address address = request.getAddress();

        List<OrderItem> orderItemList = createOrderItemList(itemSimpleInfoList);
        Order order = constructOrder(userUuid, orderItemList);
        Delivery delivery = constructDelivery(transceiverInfo, address);
        order.createDelivery(delivery);

        DeliveryTracking deliveryTracking = constructDefaultDeliveryTracking(delivery);

        // itemServiceClient.orderItems() 파라미터 추출
        List<String> itemUuidList = new ArrayList<>();
        List<Integer> itemCountList = new ArrayList<>();
        itemSimpleInfoList.forEach(info -> {
            itemUuidList.add(info.getItemUuid());
            itemCountList.add(info.getCount());
        });

        try {
            Order savedOrder = orderRepository.save(order);
            List<OrderItem> savedOrderItems = orderItemRepository.saveAll(orderItemList);
            DeliveryTracking savedDeliveryTracking = deliveryTrackingRepository.save(deliveryTracking);
            Delivery savedDelivery = savedOrder.getDelivery();
//            itemServiceClient.orderItems(itemUuidList, itemCountList);
            resilientItemServiceClient.orderItems(itemUuidList, itemCountList);
            /* Publish kafka message */
            OrderTopicMessage orderTopicMessage = orderMapper.toOrderTopicMessage(savedOrder, savedOrderItems, savedDelivery.getId(), null);
            applicationEventPublisher.publishEvent(new OrderEvent(this, savedOrder.getId(), orderTopicMessage.getPayload(), OperationType.CREATE));
            DeliveryTopicMessage deliveryTopicMessage = deliveryMapper.toDeliveryTopicMessage(savedDelivery, List.of(savedDeliveryTracking), null);
            applicationEventPublisher.publishEvent(new DeliveryEvent(this, savedDelivery.getId(), deliveryTopicMessage.getPayload(), OperationType.CREATE));
        } catch (RetryException e) {
            throw e;
        } catch (RecordException e) {
            throw e;
//        } catch (IgnoreException e) {
//            throw e;
        } catch (Exception e) {
            throw new RuntimeException(messageUtil.getOrderCreateErrorMessage());
        }
    }

    @Override
    @Transactional
    public void cancelOrder(String userUuid, String orderUuid) throws InterruptedException {
        List<String> itemUuidList = new ArrayList<>();
        List<Integer> itemCountList = new ArrayList<>();
        try {
            orderRepository.updateOrderStatusAsCancel(userUuid, orderUuid);
            Order order = orderRepository.findByOrderUuid(orderUuid).get();
            List<OrderItem> orderItems = order.getOrderItems();
            List<ItemSimpleInfo> itemSimpleInfoList = new ArrayList<>();
            orderItems.forEach(orderItem -> itemSimpleInfoList.add(ItemSimpleInfo.getInstance(orderItem)));

            // itemServiceClient.cancelOrderItems() 파라미터 추출
            itemSimpleInfoList.forEach(info -> {
                itemUuidList.add(info.getItemUuid());
                itemCountList.add(info.getCount());
            });

//            itemServiceClient.cancelOrderItems(itemUuidList, itemCountList);
            resilientItemServiceClient.cancelOrderItems(itemUuidList, itemCountList);
            /* Kafka produce */
            applicationEventPublisher.publishEvent(new OrderEvent(this, order.getId(), null, OperationType.DELETE));
            applicationEventPublisher.publishEvent(new DeliveryEvent(this, order.getDelivery().getId(), null, OperationType.DELETE));
        } catch (RetryException e) {
            throw e;
        } catch (RecordException e) {
            throw e;
//        } catch (IgnoreException e) {
//            throw e;
        } catch (Exception e) {
            throw new RuntimeException(messageUtil.getOrderCancelErrorMessage());
        }
//        resilientItemServiceClient.cancelOrderItems(itemUuidList, itemCountList);
    }

    @Override
    public OrderListResponse getOrderList(String userUuid) throws InterruptedException {
        List<OrderSimple> orderSimpleList = new ArrayList<>();
        List<String> itemUuidList;
        try {
            List<Order> orderList = orderRepository.findOrdersWithItemsAndDeliveryByUserUuid(userUuid);
            for (Order order : orderList) {
                List<OrderItemSimple> oisList = new ArrayList<>();
                List<OrderItem> orderItems = order.getOrderItems();
                for (OrderItem orderItem : orderItems) {
                    oisList.add(OrderItemSimple.getInstance(orderItem.getItemUuid(), orderItem.getCount(), orderItem.getOrderPrice()));
                }
                orderSimpleList.add(OrderSimple.getInstance(oisList, order.getDelivery().getDeliveryStatus(), order.getOrderDate()));
            }
            // itemUuidList 추출 및 itemServiceClient.getItemSimpleWithoutPrice() 호출
            itemUuidList = extractItemUuidList(orderSimpleList);
        } catch (Exception e) {
            throw new NoSuchElementException(messageUtil.getNoSuchUserUuidErrorMessage(userUuid));
        }
        List<ItemSimpleWithoutPriceResponse> itemSimpleWxPList = resilientItemServiceClient.getItemSimpleWithoutPrice(itemUuidList);
        // itemName, itemImageUrl 추출 및 삽입
        insertItemNameAndImageUrlIntoOrderSimpleList(itemSimpleWxPList, orderSimpleList);
        return OrderListResponse.getInstance(orderSimpleList);
    }

    private static List<String> extractItemUuidList(List<OrderSimple> orderSimpleList) {
        List<String> itemUuidList = new ArrayList<>();
        orderSimpleList.forEach(os -> os.getOrderItemSimpleList()
                .forEach(ois -> itemUuidList.add(ois.getItemUuid())));
        return itemUuidList;
    }

    private static void insertItemNameAndImageUrlIntoOrderSimpleList(List<ItemSimpleWithoutPriceResponse> itemSimpleWxPList, List<OrderSimple> orderSimpleList) {
        Map<String, ItemSimpleWithoutPriceResponse> itemSimpleWxPMap = new HashMap<>();
        itemSimpleWxPList.forEach(item -> itemSimpleWxPMap.put(item.getItemUuid(), item));
        orderSimpleList.forEach(os -> os.getOrderItemSimpleList()
                .forEach(ois -> {
                    String itemUuid = ois.getItemUuid();
                    if (itemSimpleWxPMap.get(itemUuid) != null) {
                        ois.setItemName(itemSimpleWxPMap.get(itemUuid).getItemName());
                        ois.setItemImageUrl(itemSimpleWxPMap.get(itemUuid).getItemImageUrl());
                    }
                }));
    }

    @Override
    public OrderDetailResponse getOrderDetail(String userUuid, String orderUuid) {
        Order order = orderRepository.findOrderWithItemsAndDeliveryByUserUuidAndOrderUuid(userUuid, orderUuid).orElseThrow(() -> {
            throw new NoSuchElementException("Invalid Order"); });

        List<OrderItemSimple> orderItemSimpleList = new ArrayList<>();
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            orderItemSimpleList.add(OrderItemSimple.getInstance(orderItem.getItemUuid(), orderItem.getCount(), orderItem.getOrderPrice()));
        }
        Delivery delivery = order.getDelivery();

        return OrderDetailResponse.getInstance(orderItemSimpleList, order.getOrderDate(),
                delivery.getDeliveryStatus(), delivery.getTransceiverInfo(), delivery.getAddress());
    }

    /** getOrderDetail - @Async, CompletableFuture 테스트 적용 **/
    @Override
    @Async
    public CompletableFuture<OrderDetailResponse> getOrderDetailAsync(String userUuid, String orderUuid) {
        return CompletableFuture.supplyAsync(() -> {
            Order order = orderRepository.findOrderWithItemsAndDeliveryByUserUuidAndOrderUuid(userUuid, orderUuid).orElseThrow(() -> {
                throw new NoSuchElementException("Invalid Order");
            });

            List<OrderItemSimple> orderItemSimpleList = new ArrayList<>();
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                orderItemSimpleList.add(OrderItemSimple.getInstance(orderItem.getItemUuid(), orderItem.getCount(), orderItem.getOrderPrice()));
            }
            Delivery delivery = order.getDelivery();

            return OrderDetailResponse.getInstance(orderItemSimpleList, order.getOrderDate(),
                    delivery.getDeliveryStatus(), delivery.getTransceiverInfo(), delivery.getAddress());
        });
    }

    @Override
    public DeliveryTrackingResponse getDeliveryTrackingInfo(String userUuid, String orderUuid) {
        Order order = orderRepository.findOrderWithDeliveryByUserUuidAndOrderUuid(userUuid, orderUuid).orElseThrow(() -> {
            throw new NoSuchElementException("Invalid Order"); });

        Delivery delivery = order.getDelivery();
//        List<DeliveryTracking> deliveryTrackings = deliveryTrackingRepository.findAllByDelivery(delivery);
        DeliveryDocument deliveryDocument = deliveryDocumentRepository.findById(delivery.getId()).get();
        List<DeliveryTracking> deliveryTrackings = deliveryTrackingMapper.toDeliveryTrackingList(deliveryDocument.getDeliveryTrackings());

        List<DeliveryTrackingInfo> deliveryTrackingInfoList = new ArrayList<>();
        for (DeliveryTracking tracking : deliveryTrackings) {
            deliveryTrackingInfoList.add(DeliveryTrackingInfo
                    .getInstance(tracking.getCourier(), tracking.getContactNumber(), tracking.getPostLocation(), tracking.getPostDateTime()));
        }

        return DeliveryTrackingResponse.getInstance(deliveryTrackingInfoList, delivery.getDeliveryStatus());
    }

    @Override
    @Transactional
    public void updateDeliveryInfo(String orderUuid, DeliveryStatusPostRequest request) {
        try {
            Delivery delivery = orderRepository.updateDeliveryStatus(orderUuid, request.getDeliveryStatus());

            DeliveryTrackingDto deliveryTrackingDto = DeliveryTrackingDto.getInstanceForCreate(request.getCourier(), request.getContactNumber(), request.getPostLocation());
            DeliveryTracking deliveryTracking = deliveryMapper.toDeliveryTracking(deliveryTrackingDto, delivery);
            DeliveryTracking savedDeliveryTracking = deliveryTrackingRepository.save(deliveryTracking);
            /* Publish kafka message */
            DeliveryTopicMessage deliveryTopicMessage = deliveryMapper.toDeliveryTopicMessage(delivery, List.of(savedDeliveryTracking), null);
            applicationEventPublisher.publishEvent(new DeliveryEvent(this, delivery.getId(), deliveryTopicMessage.getPayload(), OperationType.UPDATE));
        } catch (Exception e) {
            throw new RuntimeException(messageUtil.getDeliveryUpdateErrorMessage());
        }
    }

    private List<OrderItem> createOrderItemList(List<ItemSimpleInfo> itemSimpleInfoList) {
        List<OrderItemDto> orderItemDtoList = new ArrayList<>();
        itemSimpleInfoList.forEach(info ->
                orderItemDtoList.add(OrderItemDto.getInstanceForCreate(info)));

        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemDtoList.forEach(orderItemDto ->
                orderItemList.add(orderMapper.toOrderItem(orderItemDto)));
        return orderItemList;
    }

    private Order constructOrder(String userUuid, List<OrderItem> orderItemList) {
        OrderDto orderDto = OrderDto.getInstanceForCreate(userUuid);
        Order order = orderMapper.toOrder(orderDto);
        orderItemList.forEach(orderItem -> orderItem.createOrder(order));
        return order;
    }

    private Delivery constructDelivery(TransceiverInfo transceiverInfo, Address address) {
        DeliveryDto deliveryDto = DeliveryDto.getInstanceForCreate(transceiverInfo, address);
        return deliveryMapper.toDelivery(deliveryDto);
    }

    private DeliveryTracking constructDefaultDeliveryTracking(Delivery delivery) {
        DeliveryTrackingDto deliveryTrackingDto =
                DeliveryTrackingDto.getInstanceForCreate("kim", "010-xxxx-xxxx", "seoul");
        return deliveryMapper.toDeliveryTracking(deliveryTrackingDto, delivery);
    }
}
