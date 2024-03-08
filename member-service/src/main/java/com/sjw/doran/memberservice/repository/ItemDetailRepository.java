package com.sjw.doran.memberservice.repository;

import com.sjw.doran.memberservice.entity.ItemDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemDetailRepository extends JpaRepository<ItemDetail, Long>, ItemDetailRepositoryCustom {
}
