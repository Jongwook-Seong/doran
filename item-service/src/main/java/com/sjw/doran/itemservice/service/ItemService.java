package com.sjw.doran.itemservice.service;

import com.sjw.doran.itemservice.dto.BookDto;
import com.sjw.doran.itemservice.entity.Item;
import com.sjw.doran.itemservice.vo.response.ItemSimpleResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ItemService {

    @Transactional
    void saveBook(BookDto bookDto);

    @Transactional
    void deleteItem(String itemUuid);

    @Transactional(readOnly = true)
    Item getItemDetail(String itemUuid);

    @Transactional(readOnly = true)
    List<ItemSimpleResponse> getBooksByKeyword(String keyword);
}
