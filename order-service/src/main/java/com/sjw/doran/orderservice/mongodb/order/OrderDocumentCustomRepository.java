package com.sjw.doran.orderservice.mongodb.order;

import java.util.List;

public interface OrderDocumentCustomRepository {

    List<OrderDocument> findAllByUserUuid(String userUuid);
}
