package com.sjw.doran.authservice.dto;

import com.sjw.doran.authservice.entity.constant.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {

    private String identity;
    private String password;
    private String email;
    private String username;
    private LocalDate birthDate;
    private Gender gender;
}
