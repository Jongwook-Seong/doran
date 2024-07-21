package com.sjw.doran.memberservice.kafka.member;

import com.sjw.doran.memberservice.kafka.common.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberTopicMessage {

    private Long id;
    private Payload payload;
    private OperationType operationType;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payload {

        private Long id;
        private String userUuid;
        private String nickname;
        private String profileImageUrl;
        private AddressData address;
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
