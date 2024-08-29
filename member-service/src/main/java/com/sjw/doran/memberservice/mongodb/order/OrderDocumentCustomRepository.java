package com.sjw.doran.memberservice.mongodb.order;

import java.util.List;
import java.util.Optional;

public interface OrderDocumentCustomRepository {

    List<OrderDocument> findAllByUserUuid(String userUuid);

    Optional<OrderDocument> findByUserUuidAndOrderUuid(String userUuid, String orderUuid);
}
