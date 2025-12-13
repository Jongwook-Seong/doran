package com.sjw.doran.authservice.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleInfoResponse {

    private String clientId;
    private String redirectUri;
}
