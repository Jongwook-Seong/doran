package com.sjw.doran.itemservice.service;

import com.sjw.doran.itemservice.entity.Item;
import com.sjw.doran.itemservice.vo.request.BookCreateRequest;
import com.sjw.doran.itemservice.vo.response.ItemSimpleResponse;
import com.sjw.doran.itemservice.vo.response.ItemSimpleWithQuantityResponse;
import com.sjw.doran.itemservice.vo.response.ItemSimpleWithoutPriceResponse;

import java.util.List;

public interface ItemService {

    void saveBook(BookCreateRequest request);

    void deleteItem(String itemUuid);

    Item getItemDetail(String itemUuid);

    List<ItemSimpleResponse> getItemSimpleList(List<String> itemUuidList);

    List<ItemSimpleWithQuantityResponse> getItemSimpleWithQuantityList(List<String> itemUuidList);

    List<ItemSimpleWithoutPriceResponse> getItemSimpleWithoutPriceList(List<String> itemUuidList);

    List<ItemSimpleResponse> getBooksByKeyword(String keyword);

    void subtractItems(List<String> itemUuidList, List<Integer> countList);

    void restoreItems(List<String> itemUuidList, List<Integer> countList);
}
