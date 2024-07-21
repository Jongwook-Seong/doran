package com.sjw.doran.orderservice.kafka.delivery;

import com.sjw.doran.orderservice.entity.DeliveryStatus;
import com.sjw.doran.orderservice.kafka.common.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryTopicMessage {

    private Long id;
    private Payload payload;
    private OperationType operationType;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payload {

        private Long id;
        private DeliveryStatus deliveryStatus;
        private TransceiverInfoData transceiverInfo;
        private AddressData address;
        private List<DeliveryTrackingData> deliveryTrackings;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransceiverInfoData {

        private String ordererName;
        private String receiverName;
        private String receiverPhoneNumber;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressData {

        private String city;
        private String street;
        private String details;
        private String zipcode;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeliveryTrackingData {

        private String courier;
        private String contactNumber;
        private String postLocation;
        private LocalDateTime postDateTime;
    }
}
