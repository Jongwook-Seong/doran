package com.sjw.doran.authservice.vo.request;

import com.sjw.doran.authservice.entity.constant.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinRequest {

    private String identity;
    private String password;
    private String email;
    private String username;
    private LocalDate birthDate;
    private Gender gender;
}
