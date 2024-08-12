package com.sjw.doran.itemservice.mapper;

import com.sjw.doran.itemservice.entity.Artwork;
import com.sjw.doran.itemservice.kafka.common.OperationType;
import com.sjw.doran.itemservice.kafka.item.ItemTopicMessage;
import com.sjw.doran.itemservice.mongodb.item.ItemDocument;
import com.sjw.doran.itemservice.redis.data.BestItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring", imports = UUID.class)
public interface ArtworkMapper {

    ArtworkMapper INSTANCE = Mappers.getMapper(ArtworkMapper.class);

    @Mapping(target = "id", source = "artwork.id")
    @Mapping(target = "operationType", source = "operationType")
    @Mapping(target = "payload", expression = "java(mapPayload(artwork, orderQuantity))")
    ItemTopicMessage toItemTopicMessage(Artwork artwork, int orderQuantity, OperationType operationType);

    @Mapping(target = "artist", source = "artwork.artist")
    @Mapping(target = "explanation", source = "artwork.explanation")
    @Mapping(target = "workSize", source = "artwork.workSize")
    @Mapping(target = "productionYear", source = "artwork.productionYear")
    ItemTopicMessage.ArtworkData toItemTopicMessageArtworkData(Artwork artwork);

    @Mapping(target = "id", source = "artwork.id")
    @Mapping(target = "itemUuid", source = "artwork.itemUuid")
    @Mapping(target = "itemName", source = "artwork.itemName")
    @Mapping(target = "price", source = "artwork.price")
    @Mapping(target = "stockQuantity", source = "artwork.stockQuantity")
    @Mapping(target = "itemImageUrl", source = "artwork.itemImageUrl")
    @Mapping(target = "category", source = "artwork.category")
    @Mapping(target = "artworkData", source = "artwork")
    @Mapping(target = "createdAt", source = "artwork.createdAt")
    @Mapping(target = "modifiedAt", source = "artwork.modifiedAt")
    @Mapping(target = "latestOrderQuantity", source = "orderQuantity")
    ItemTopicMessage.Payload mapPayload(Artwork artwork, int orderQuantity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "itemUuid", source = "itemUuid")
    @Mapping(target = "itemName", source = "itemName")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "stockQuantity", source = "stockQuantity")
    @Mapping(target = "itemImageUrl", source = "itemImageUrl")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "artworkInfo.artist", source = "artworkData.artist")
    @Mapping(target = "artworkInfo.explanation", source = "artworkData.explanation")
    @Mapping(target = "artworkInfo.workSize", source = "artworkData.workSize")
    @Mapping(target = "artworkInfo.productionYear", source = "artworkData.productionYear")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "modifiedAt", source = "modifiedAt")
    ItemDocument toItemDocument(ItemTopicMessage.Payload payload);

    BestItem.ArtworkInfo toBestItemArtworkInfo(ItemDocument.ArtworkInfo artworkInfo);
}
