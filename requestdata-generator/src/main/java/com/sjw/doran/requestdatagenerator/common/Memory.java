package com.sjw.doran.requestdatagenerator.common;

import com.sjw.doran.requestdatagenerator.item.service.ItemRequestGenerateService;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Data
public class Memory {

    private static final Memory instance = new Memory();

    private Memory() {
    }

    public static Memory getInstance() {
        return instance;
    }

    public final Map<Long, Map<String, Object>> memberDataMap = new HashMap<>();
    public final Map<Long, Map<String, Object>> basketDataMap = new HashMap<>();
    public final Map<Long, Map<String, Object>> basketItemDataMap = new HashMap<>();
    public final Map<Long, Map<String, Object>> orderDataMap = new HashMap<>();
    public final Map<Long, Map<String, Object>> orderItemDataMap = new HashMap<>();
    public final Map<Long, Map<String, Object>> deliveryDataMap = new HashMap<>();
    public final Map<Long, Map<String, Object>> deliveryTrackingDataMap = new HashMap<>();
    public final Map<Long, ItemRequestGenerateService.BookItem> itemDataMap = new HashMap<>();
}
