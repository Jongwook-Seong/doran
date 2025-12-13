package com.sjw.doran.authservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private String userUuid;
    private String userId;
    private String email;
    private String phoneNumber;
    private String username;
}
