package com.sjw.doran.authsimpleservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private String userUuid;
    private String userId;
    private String email;
    private String username;
}
