package com.sjw.doran.memberservice.vo.request.item;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemListRequest {

    private List<String> itemUuidList;
}
