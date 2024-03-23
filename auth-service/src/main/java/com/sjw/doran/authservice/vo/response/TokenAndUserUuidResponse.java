package com.sjw.doran.authservice.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenAndUserUuidResponse {

    private String accessToken;
    private String refreshToken;
    private String userUuid;
}
