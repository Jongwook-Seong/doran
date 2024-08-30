package com.sjw.doran.orderservice.kafka.item;

import com.sjw.doran.orderservice.kafka.common.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemTopicMessage {

    private Long id;
    private Payload payload;
    private OperationType operationType;

    @Data
    @RequiredArgsConstructor
    public static class Payload {

        private Long id;
        private String itemUuid;
        private String itemName;
        private int price;
        private int stockQuantity;
        private String itemImageUrl;
        private Category category;

        private BookData bookData;
        private ArtworkData artworkData;

        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

        private int latestOrderQuantity;
    }

    public enum Category {
        BOOK, ARTWORK;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookData {

        private String author;
        private String isbn;
        private int pages;
        private Date publicationDate;
        private String contentsTable;
        private String bookReview;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArtworkData {

        private String artist;
        private String explanation;
        private String workSize;
        private int productionYear;
    }
}
