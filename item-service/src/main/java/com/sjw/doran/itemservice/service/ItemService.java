package com.sjw.doran.itemservice.service;

import com.sjw.doran.itemservice.dto.BookDto;
import com.sjw.doran.itemservice.entity.Item;
import com.sjw.doran.itemservice.vo.response.ItemSimpleResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ItemService {

    void saveBook(BookDto bookDto);

    void deleteItem(String itemUuid);

    Item getItemDetail(String itemUuid);

    Slice<ItemSimpleResponse> getItemSimpleSlice(List<String> itemUuidList, Pageable pageable);

    List<ItemSimpleResponse> getBooksByKeyword(String keyword);
}
