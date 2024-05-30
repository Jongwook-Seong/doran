package com.sjw.doran.memberservice.vo.response.order;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransceiverInfo {

    private String ordererName;
    private String receiverName;
    private String receiverPhoneNumber;
}
