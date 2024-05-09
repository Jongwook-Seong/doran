package com.sjw.doran.authsimpleservice.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class UserJoinRequest {

    @NotNull(message = "아이디는 필수 입력값입니다.")
    @Size(min = 3, message = "아이디는 반드시 세 글자 이상이어야 합니다.")
    private String userId;

    @NotNull(message = "비밀번호는 필수 입력값입니다.")
    @Size(min = 4, message = "비밀번호는 반드시 네 글자 이상이어야 합니다.")
    private String password;

    @NotNull(message = "이름은 필수 입력값입니다.")
    @Size(min = 2, message = "이름은 반드시 두 글자 이상이어야 합니다.")
    private String username;
    @NotNull(message = "생년월일은 필수 입력값입니다.")
    private Date birthDate;
    @NotNull(message = "이메일은 필수 입력값입니다.")
    @Size(min = 3, message = "이메일은 반드시 세 글자 이상이어야 합니다.")
    @Email
    private String email;
    @NotNull(message = "연락처는 필수 입력값입니다.")
    private String phoneNumber;
}
