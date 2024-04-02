package com.sjw.doran.orderservice.vo.request;

import com.sjw.doran.orderservice.vo.ItemSimpleInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {

    private List<ItemSimpleInfo> itemSimpleInfoList;
}
