package com.sjw.doran.memberservice.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberOrderResponse {

    String userUuid;
    String nickname;
    String profileImageUrl;
    Object orderResponse;

    public static MemberOrderResponse getInstance(String userUuid, String nickname, String profileImageUrl, Object response) {
        return MemberOrderResponse.builder()
                .userUuid(userUuid)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .orderResponse(response)
                .build();
    }
}
