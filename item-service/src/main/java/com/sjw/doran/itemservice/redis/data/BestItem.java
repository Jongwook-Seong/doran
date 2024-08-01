package com.sjw.doran.itemservice.redis.data;

import com.sjw.doran.itemservice.entity.Category;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@RedisHash(value = "best_item:v1", timeToLive = 7200)
//@RedisHash(value = "best_item:v1", timeToLive = 1800)
public class BestItem implements Serializable {

    @Id
    private String itemUuid;
    private String itemName;
    private int price;
    private String itemImageUrl;
    private Category category;

    private BookInfo bookInfo;
    private ArtworkInfo artworkInfo;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookInfo implements Serializable {

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
    public static class ArtworkInfo implements Serializable {

        private String artist;
        private String explanation;
        private String workSize;
        private int productionYear;
    }
}
