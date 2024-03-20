package com.sjw.doran.authservice.dto;

import com.sjw.doran.authservice.entity.constant.Gender;
import com.sjw.doran.authservice.entity.constant.UserRole;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String userUuid;
    private String identity;
    private String password;
    private String email;
    private String username;
    private LocalDate birthDate;
    private Gender gender;
    private UserRole role;
}
