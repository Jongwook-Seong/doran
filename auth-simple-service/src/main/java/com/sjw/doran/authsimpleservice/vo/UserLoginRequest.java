package com.sjw.doran.authsimpleservice.vo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequest {

    @NotNull(message = "아이디는 필수 입력값입니다.")
    @Size(min = 3, message = "아이디는 반드시 세 글자 이상이어야 합니다.")
    private String userId;

    @NotNull(message = "비밀번호는 필수 입력값입니다.")
    @Size(min = 4, message = "비밀번호는 반드시 네 글자 이상이어야 합니다.")
    private String password;
}
