package com.sjw.doran.authservice.dto;

import com.sjw.doran.authservice.entity.UserRole;
import lombok.Data;

import java.util.Date;

@Data
public class UserDto {

    private String userUuid;
    private String username;
    private Date birthDate;
    private String email;
    private String phoneNumber;

    private UserRole role;
    private String userId;
    private String password;

    private boolean activated;
    private boolean isDeleted;
}
