package com.sjw.doran.memberservice.vo.response.order;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor
public class TransceiverInfo {

    private String ordererName;
    private String receiverName;
    private String receiverPhoneNumber;

    @Builder
    public TransceiverInfo(String ordererName, String receiverName, String receiverPhoneNumber) {
        this.ordererName = ordererName;
        this.receiverName = receiverName;
        this.receiverPhoneNumber = receiverPhoneNumber;
    }
}
