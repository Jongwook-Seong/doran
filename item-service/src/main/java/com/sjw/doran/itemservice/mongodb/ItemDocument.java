package com.sjw.doran.itemservice.mongodb;

import com.sjw.doran.itemservice.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "itemInfo")
@Data
@AllArgsConstructor
public class ItemDocument {

    @Id
    private Long id;
    private String itemUuid;
    private String itemName;
    private int price;
    private int stockQuantity;
    private String itemImageUrl;
    private Category category;

    private BookInfo bookInfo;
    private ArtworkInfo artworkInfo;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookInfo {

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
    public static class ArtworkInfo {

        private String artist;
        private String explanation;
        private String workSize;
        private int productionYear;
    }
}
