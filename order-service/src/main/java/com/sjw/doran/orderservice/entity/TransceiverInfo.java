package com.sjw.doran.orderservice.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TransceiverInfo {

    private String ordererName;
    private String receiverName;
    private String receiverPhoneNumber;
}
