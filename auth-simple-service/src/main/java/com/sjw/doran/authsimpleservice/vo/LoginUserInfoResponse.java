package com.sjw.doran.authsimpleservice.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserInfoResponse {
    
    private String userUuid;      // 사용자 UUID
    private String userId;        // 사용자 ID
    private String username;      // 사용자 이름
    private String email;         // 이메일
    private String token;         // JWT 토큰
    private String message;       // 성공 메시지
}


