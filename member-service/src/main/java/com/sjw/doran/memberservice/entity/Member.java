package com.sjw.doran.memberservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "idx_user_uuid", columnList = "user_uuid"))
public class Member extends AuditingFields {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String userUuid;
    private String nickname;
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
