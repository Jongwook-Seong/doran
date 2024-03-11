package com.sjw.doran.memberservice.dto;

import lombok.*;

@Data
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class MemberDto {

    String userUuid;
    String nickname;
    String profileImageUrl;
}
