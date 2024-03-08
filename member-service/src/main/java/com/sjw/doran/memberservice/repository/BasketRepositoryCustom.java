package com.sjw.doran.memberservice.repository;

import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.Member;
import org.springframework.transaction.annotation.Transactional;

public interface BasketRepositoryCustom {

    @Transactional(readOnly = true)
    Basket findByMember(Member member);
}
