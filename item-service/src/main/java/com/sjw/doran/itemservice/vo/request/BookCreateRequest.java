package com.sjw.doran.itemservice.vo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookCreateRequest {

    // 아이템 이미지 Base64 인코딩 문자열
    private String fileData;

    @NotBlank(message = "{request.required}")
    private String itemName;
    private int price;
    private int stockQuantity;

    @NotBlank(message = "{request.required}")
    private String author;
    @NotBlank(message = "{request.required}")
    private String isbn;
    private int pages;
    private Date publicationDate;
    private String contentsTable;
    private String bookReview;
}
