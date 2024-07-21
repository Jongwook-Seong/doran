package com.sjw.doran.orderservice.mongodb;

import com.sjw.doran.orderservice.entity.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "deliveryInfoTrackingList")
@Data
@AllArgsConstructor
public class DeliveryDocument {

    @Id
    private Long id;
    private DeliveryStatus deliveryStatus;
    private TransceiverInfo transceiverInfo;
    private Address address;
    private List<DeliveryTracking> deliveryTrackings;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransceiverInfo {

        private String ordererName;
        private String receiverName;
        private String receiverPhoneNumber;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {

        private String city;
        private String street;
        private String details;
        private String zipcode;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeliveryTracking {

        private String courier;
        private String contactNumber;
        private String postLocation;
        private LocalDateTime postDateTime;
    }
}
