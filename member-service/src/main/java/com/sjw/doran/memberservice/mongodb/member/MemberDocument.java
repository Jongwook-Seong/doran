package com.sjw.doran.memberservice.mongodb.member;

import com.sjw.doran.memberservice.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "memberInfo")
@Data
@AllArgsConstructor
public class MemberDocument {

    @Id
    private Long id;
    private String userUuid;
    private String nickname;
    private String profileImageUrl;
    private Address address;
}
