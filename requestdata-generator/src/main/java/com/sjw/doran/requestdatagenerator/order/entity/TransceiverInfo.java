package com.sjw.doran.requestdatagenerator.order.entity;

import jakarta.persistence.Column;
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

    @Column(name = "orderer_name")
    private String ordererName;
    @Column(name = "receiver_name")
    private String receiverName;
    @Column(name = "receiver_phone_number")
    private String receiverPhoneNumber;
}
