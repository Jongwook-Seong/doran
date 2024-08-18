package com.sjw.doran.requestdatagenerator.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @Column(name = "user_uuid")
    private String userUuid;
    private String nickname;
    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Embedded
    private Address address;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Basket basket;

    @Builder
    public Member(String userUuid, String username) {
        this.userUuid = userUuid;
        this.nickname = username;
    }

    @Builder
    public Member(String userUuid, String nickname, String profileImageUrl) {
        this.userUuid = userUuid;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}
