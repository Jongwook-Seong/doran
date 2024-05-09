package com.sjw.doran.authsimpleservice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserDto {

    private String userUuid;
    private String username;
    private Date birthDate;
    private String email;
    private String phoneNumber;

    private String userId;
    private String password;

    private String encryptedPwd;
}
