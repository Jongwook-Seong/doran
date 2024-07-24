package com.sjw.doran.itemservice.redis.service;

import com.sjw.doran.itemservice.mapper.ItemMapper;
import com.sjw.doran.itemservice.mongodb.incidental.IncidentalDocument;
import com.sjw.doran.itemservice.mongodb.incidental.IncidentalDocumentRepository;
import com.sjw.doran.itemservice.mongodb.item.ItemDocument;
import com.sjw.doran.itemservice.mongodb.item.ItemDocumentRepository;
import com.sjw.doran.itemservice.redis.data.BestItem;
import com.sjw.doran.itemservice.redis.data.OrderedItemSales;
import com.sjw.doran.itemservice.redis.repository.BestItemRedisRepository;
import com.sjw.doran.itemservice.redis.repository.OrderedItemSalesRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BestItemCacheServiceImpl implements BestItemCacheService {

    private final OrderedItemSalesRedisRepository orderedItemSalesRedisRepository;
    private final BestItemRedisRepository bestItemRedisRepository;
    private final ItemDocumentRepository itemDocumentRepository;
    private final IncidentalDocumentRepository incidentalDocumentRepository;
    private final ItemMapper itemMapper;

    @Scheduled(cron = "0 0 0/1 * * *")
    public void setOrderedItemSalesOnRedis() {
        this.setOrderedItemSales();
    }

    @Scheduled(cron = "0 0 0/1 * * *")
    public void setBestItemsOnRedis() {
        this.setBestItems();
    }

    @Override
    public List<BestItem> getBestItems() {
        Iterable<BestItem> findBestItemIterable = bestItemRedisRepository.findAll();
        List<BestItem> bestItems = new ArrayList<>();
        findBestItemIterable.forEach(item -> bestItems.add(item));
        return bestItems;
    }

    @Override
    public void setBestItems() {
        Iterable<OrderedItemSales> allItemSalesIterable = orderedItemSalesRedisRepository.findAll();
        List<OrderedItemSales> allItemSalesList = new ArrayList<>();
        allItemSalesIterable.forEach(itemSales -> allItemSalesList.add(itemSales));
        List<OrderedItemSales> bestItemSalesList = allItemSalesList.stream()
                .sorted(Comparator.comparingLong(OrderedItemSales::getSales).reversed())
                .limit(10)
                .collect(Collectors.toList());
        List<String> bestItemUuidList = new ArrayList<>();
        bestItemSalesList.forEach(itemSales -> bestItemUuidList.add(itemSales.getItemUuid()));
        List<ItemDocument> itemDocumentList = itemDocumentRepository.findByItemUuidList(bestItemUuidList);
        bestItemRedisRepository.deleteAll();
        bestItemRedisRepository.saveAll(itemMapper.toBestItemList(itemDocumentList));
    }

    @Override
    public void setOrderedItemSales() {
        List<IncidentalDocument> allIncidentalDocuments = incidentalDocumentRepository.findAll();
        orderedItemSalesRedisRepository.deleteAll();
        for (IncidentalDocument incidentalDocument : allIncidentalDocuments) {
            long sales = 0L;
            List<IncidentalDocument.OrderIncidentalData> incidentalDataList = incidentalDocument.getIncidentalDataList();
            for (IncidentalDocument.OrderIncidentalData orderIncidentalData : incidentalDataList)
                sales += orderIncidentalData.getOrderQuantity();
            orderedItemSalesRedisRepository.save(new OrderedItemSales(incidentalDocument.getItemUuid(), sales));
        }
    }
}
