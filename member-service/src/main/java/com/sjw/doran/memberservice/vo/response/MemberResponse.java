package com.sjw.doran.memberservice.vo.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberResponse {

    private String userUuid;
    private String message;

    public static MemberResponse getInstance(String userUuid, String message) {
        return MemberResponse.builder()
                .userUuid(userUuid)
                .message(message)
                .build();
    }
}
