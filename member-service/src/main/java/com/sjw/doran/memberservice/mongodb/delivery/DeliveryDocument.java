package com.sjw.doran.memberservice.mongodb.delivery;

import com.sjw.doran.memberservice.vo.response.order.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "deliveryInfoForMemberService")
@Data
@AllArgsConstructor
public class DeliveryDocument {

    @Id
    private Long id;
    private List<DeliveryTrackingInfo> deliveryTrackingInfoList;
    private TransceiverInfoData transceiverInfoData;
    private AddressData address;
    private DeliveryStatus deliveryStatus;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeliveryTrackingInfo {

        private String courier;
        private String contactNumber;
        private String postLocation;
        private LocalDateTime postDateTime;
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
}
