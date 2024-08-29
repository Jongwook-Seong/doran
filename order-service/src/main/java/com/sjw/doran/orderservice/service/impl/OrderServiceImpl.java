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
import com.sjw.doran.orderservice.mapper.*;
import com.sjw.doran.orderservice.mongodb.delivery.DeliveryDocument;
import com.sjw.doran.orderservice.mongodb.delivery.DeliveryDocumentRepository;
import com.sjw.doran.orderservice.mongodb.item.ItemDocument;
import com.sjw.doran.orderservice.mongodb.item.ItemDocumentRepository;
import com.sjw.doran.orderservice.mongodb.order.OrderDocument;
import com.sjw.doran.orderservice.mongodb.order.OrderDocumentRepository;
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
    private final OrderDocumentRepository orderDocumentRepository;
    private final DeliveryDocumentRepository deliveryDocumentRepository;
    private final ItemDocumentRepository itemDocumentRepository;
    private final ResilientItemServiceClient resilientItemServiceClient;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final OrderMapper orderMapper;
    private final DeliveryMapper deliveryMapper;
    private final DeliveryTrackingMapper deliveryTrackingMapper;
    private final TransceiverInfoMapper transceiverInfoMapper;
    private final AddressMapper addressMapper;
    private final ItemMapper itemMapper;
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
            resilientItemServiceClient.orderItems(itemUuidList, itemCountList);
            System.out.println("resilientItemServiceClient.orderItems");
            /* Publish kafka message */
            OrderTopicMessage orderTopicMessage = orderMapper.toOrderTopicMessage(savedOrder, savedOrderItems, savedDelivery.getId(), null);
            applicationEventPublisher.publishEvent(new OrderEvent(this, savedOrder.getId(), orderTopicMessage.getPayload(), OperationType.CREATE));
            System.out.println("publishEvent - OrderEvent");
            DeliveryTopicMessage deliveryTopicMessage = deliveryMapper.toDeliveryTopicMessage(savedDelivery, List.of(savedDeliveryTracking), null);
            applicationEventPublisher.publishEvent(new DeliveryEvent(this, savedDelivery.getId(), deliveryTopicMessage.getPayload(), OperationType.CREATE));
            System.out.println("publishEvent - DeliveryEvent");
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
    @Transactional(readOnly = true)
    public OrderListResponse getOrderList(String userUuid) throws InterruptedException {
        List<OrderSimple> orderSimpleList;
        List<String> itemUuidList;
        try {
            List<OrderDocument> orderDocumentList = orderDocumentRepository.findAllByUserUuid(userUuid);
            if (orderDocumentList != null) { // first : find from MongoDB
                orderSimpleList = createOrderSimpleListByOrderDocumentList(orderDocumentList);
            } else { // second : if not exist in MongoDB, then find from MySQL DB
                List<Order> orderList = orderRepository.findOrdersWithItemsAndDeliveryByUserUuid(userUuid);
                orderSimpleList = createOrderSimpleList(orderList);
            }
            // itemUuidList 추출 및 itemServiceClient.getItemSimpleWithoutPrice() 호출
            itemUuidList = extractItemUuidList(orderSimpleList);
        } catch (Exception e) {
            throw new NoSuchElementException(messageUtil.getNoSuchUserUuidErrorMessage(userUuid));
        }
        // Get Item-Service data by Kafka Asynchronous Communication, instead of FeignClient
        List<ItemDocument> itemDocuments = itemDocumentRepository.findAllByItemUuidIn(itemUuidList);
//        List<ItemSimpleWithoutPriceResponse> itemSimpleWxPList = resilientItemServiceClient.getItemSimpleWithoutPrice(itemUuidList);
        // itemName, itemImageUrl 추출 및 삽입
        insertItemNameAndImageUrlIntoOrderSimpleList(itemMapper.toItemSimpleWxPResponseList(itemDocuments), orderSimpleList);
//        insertItemNameAndImageUrlIntoOrderSimpleList(itemSimpleWxPList, orderSimpleList);
        return OrderListResponse.getInstance(orderSimpleList);
    }

    private List<OrderSimple> createOrderSimpleList(List<Order> orderList) {
        List<OrderSimple> orderSimpleList = new ArrayList<>();
        for (Order order : orderList) {
            List<OrderItemSimple> oisList = new ArrayList<>();
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                oisList.add(OrderItemSimple.getInstance(orderItem.getItemUuid(), orderItem.getCount(), orderItem.getOrderPrice()));
            }
            orderSimpleList.add(OrderSimple.getInstance(oisList, order.getDelivery().getDeliveryStatus(), order.getOrderDate()));
        }
        return orderSimpleList;
    }

    private List<OrderSimple> createOrderSimpleListByOrderDocumentList(List<OrderDocument> orderDocumentList) {
        List<OrderSimple> orderSimpleList = new ArrayList<>();
        List<Long> deliveryIdList = new ArrayList<>();
        orderDocumentList.forEach(doc -> deliveryIdList.add(doc.getDeliveryId()));
        List<DeliveryDocument> deliveryDocumentList = deliveryDocumentRepository.findAllByIds(deliveryIdList);
        for (int i = 0; i < orderDocumentList.size(); i++) {
            List<OrderItemSimple> oisList = new ArrayList<>();
            List<OrderDocument.OrderItem> orderItems = orderDocumentList.get(i).getOrderItems();
            for (OrderDocument.OrderItem orderItem : orderItems) {
                oisList.add(OrderItemSimple.getInstance(orderItem.getItemUuid(), orderItem.getCount(), orderItem.getOrderPrice()));
            }
            orderSimpleList.add(OrderSimple.getInstance(oisList, deliveryDocumentList.get(i).getDeliveryStatus(), orderDocumentList.get(i).getOrderDate()));
        }
        return orderSimpleList;
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
    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderDetail(String userUuid, String orderUuid) {
        OrderDocument orderDocument = orderDocumentRepository.findByUserUuidAndOrderUuid(userUuid, orderUuid).get();
        if (orderDocument != null) { // first : find from MongoDB
            DeliveryDocument deliveryDocument = deliveryDocumentRepository.findById(orderDocument.getDeliveryId()).get();
            List<OrderItemSimple> orderItemSimpleList = new ArrayList<>();
            List<OrderDocument.OrderItem> orderItems = orderDocument.getOrderItems();
            for (OrderDocument.OrderItem orderItem : orderItems) {
                orderItemSimpleList.add(OrderItemSimple.getInstance(orderItem.getItemUuid(), orderItem.getCount(), orderItem.getOrderPrice()));
            }
            return OrderDetailResponse.getInstance(orderItemSimpleList, orderDocument.getOrderDate(),
                    deliveryDocument.getDeliveryStatus(),
                    transceiverInfoMapper.toTransceiverInfo(deliveryDocument.getTransceiverInfo()),
                    addressMapper.toAddress(deliveryDocument.getAddress()));
        } else { // second : if not exist in MongoDB, then find from MySQL DB
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
        }
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
    @Transactional(readOnly = true)
    public DeliveryTrackingResponse getDeliveryTrackingInfo(String userUuid, String orderUuid) {
        List<DeliveryTracking> deliveryTrackings = new ArrayList<>();
        Delivery delivery = null;
        Long deliveryId = null;
        DeliveryStatus deliveryStatus = null;

        Optional<OrderDocument> optionalOrderDocument = orderDocumentRepository.findByUserUuidAndOrderUuid(userUuid, orderUuid);
        if (optionalOrderDocument.isPresent()) { // first : find from MongoDB
            deliveryId = optionalOrderDocument.get().getDeliveryId();
        } else { // second : if not exist in MongoDB, then find from MySQL DB
            delivery = orderRepository.findOrderWithDeliveryByUserUuidAndOrderUuid(userUuid, orderUuid).orElseThrow(() -> {
                throw new NoSuchElementException("Invalid Order"); }).getDelivery();
            deliveryId = delivery.getId();
            deliveryStatus = delivery.getDeliveryStatus();
        }

        Optional<DeliveryDocument> optionalDeliveryDocument = deliveryDocumentRepository.findById(deliveryId);
        if (optionalDeliveryDocument.isPresent()) { // first : find from MongoDB
            deliveryTrackings = deliveryTrackingMapper.toDeliveryTrackingList(optionalDeliveryDocument.get().getDeliveryTrackings());
        } else { // second : if not exist in MongoDB, then find from MySQL DB
            deliveryTrackings = deliveryTrackingRepository.findAllByDelivery(delivery);
        }
        if (deliveryStatus == null)
            deliveryStatus = optionalDeliveryDocument.get().getDeliveryStatus();

        List<DeliveryTrackingInfo> deliveryTrackingInfoList = new ArrayList<>();
        for (DeliveryTracking tracking : deliveryTrackings) {
            deliveryTrackingInfoList.add(DeliveryTrackingInfo
                    .getInstance(tracking.getCourier(), tracking.getContactNumber(), tracking.getPostLocation(), tracking.getPostDateTime()));
        }

        return DeliveryTrackingResponse.getInstance(deliveryTrackingInfoList, deliveryStatus);
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
