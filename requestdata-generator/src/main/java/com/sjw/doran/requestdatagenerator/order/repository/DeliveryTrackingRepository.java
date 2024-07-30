package com.sjw.doran.requestdatagenerator.order.repository;

import com.sjw.doran.requestdatagenerator.order.entity.DeliveryTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryTrackingRepository extends JpaRepository<DeliveryTracking, Long> {
}
