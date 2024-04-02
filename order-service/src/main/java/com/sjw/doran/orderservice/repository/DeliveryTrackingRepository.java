package com.sjw.doran.orderservice.repository;

import com.sjw.doran.orderservice.entity.DeliveryTracking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryTrackingRepository extends JpaRepository<DeliveryTracking, Long>, DeliveryTrackingRepositoryCustom {
}
