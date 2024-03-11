package com.sjw.doran.memberservice.vo.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberJoinResponse {

    private String userUuid;
    private String message;

    public static MemberJoinResponse getInstance(String userUuid, String message) {
        return MemberJoinResponse.builder()
                .userUuid(userUuid)
                .message(message)
                .build();
    }
}
