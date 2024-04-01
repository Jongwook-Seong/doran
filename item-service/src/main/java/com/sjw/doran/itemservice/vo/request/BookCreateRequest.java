package com.sjw.doran.itemservice.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookCreateRequest {

    @NotBlank(message = "{request.required}")
    private String itemName;
    @NotBlank(message = "{request.required}")
    private int price;
    @NotBlank(message = "{request.required}")
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
