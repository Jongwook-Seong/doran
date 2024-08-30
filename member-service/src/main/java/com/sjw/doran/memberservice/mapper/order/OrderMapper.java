package com.sjw.doran.memberservice.mapper.order;

import com.sjw.doran.memberservice.kafka.order.OrderTopicMessage;
import com.sjw.doran.memberservice.mapper.AddressMapper;
import com.sjw.doran.memberservice.mongodb.delivery.DeliveryDocument;
import com.sjw.doran.memberservice.mongodb.item.ItemDocument;
import com.sjw.doran.memberservice.mongodb.order.OrderDocument;
import com.sjw.doran.memberservice.vo.response.order.DeliveryStatus;
import com.sjw.doran.memberservice.vo.response.order.OrderDetailResponse;
import com.sjw.doran.memberservice.vo.response.order.OrderSimple;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper(uses = { OrderItemMapper.class, TransceiverInfoMapper.class, AddressMapper.class }, componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "orderUuid", source = "payload.orderUuid")
    @Mapping(target = "userUuid", source = "payload.userUuid")
    @Mapping(target = "orderItems", source = "payload.items")
    @Mapping(target = "deliveryId", source = "payload.deliveryId")
    @Mapping(target = "orderDate", source = "payload.orderDate")
    OrderDocument toOrderDocument(OrderTopicMessage.Payload payload);

    @Mappings({
            @Mapping(target = "orderItemSimpleList", ignore = true),
            @Mapping(target = "orderDate", source = "orderDocument.orderDate"),
            @Mapping(target = "deliveryStatus", source = "deliveryStatus")
    })
    OrderSimple toOrderSimple(OrderDocument orderDocument, List<ItemDocument> itemDocuments, DeliveryStatus deliveryStatus);

    @AfterMapping
    default void mapOrderItemSimpleList(@MappingTarget OrderSimple orderSimple,
                                        OrderDocument orderDocument, List<ItemDocument> itemDocuments,
                                        @Context OrderItemMapper orderItemMapper) {
        orderSimple.setOrderItemSimpleList(orderItemMapper.toOrderItemSimpleList(orderDocument.getOrderItems(), itemDocuments));
    }

    default List<OrderSimple> toOrderSimpleList(List<OrderDocument> orderDocuments, List<List<ItemDocument>> itemDocumentsList, List<DeliveryStatus> deliveryStatuses) {
        List<OrderSimple> orderSimpleList = new ArrayList<>();
        for (int i = 0; i < orderDocuments.size(); i++)
            orderSimpleList.add(toOrderSimple(orderDocuments.get(i), itemDocumentsList.get(i), deliveryStatuses.get(i)));
        return orderSimpleList;
    }

    @Mappings({
            @Mapping(target = "orderItemSimpleList", source = "orderDocument.orderItems"),
            @Mapping(target = "orderDate", source = "orderDocument.orderDate"),
            @Mapping(target = "deliveryStatus", source = "deliveryDocument.deliveryStatus"),
            @Mapping(target = "transceiverInfo", source = "deliveryDocument.transceiverInfoData"),
            @Mapping(target = "address", source = "deliveryDocument.address")
    })
    OrderDetailResponse toOrderDetailResponse(OrderDocument orderDocument, DeliveryDocument deliveryDocument);
}
