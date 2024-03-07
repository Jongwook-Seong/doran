package com.sjw.doran.memberservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String memberUuid;
    private String nickname;
    private String profileImageUrl;

    @Embedded
    private Address address;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "order_uuid_list", joinColumns = @JoinColumn(name = "member_id"))
    private List<String> orderUuid;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Basket basket;

    public Member(String memberUuid, String nickname, String profileImageUrl) {
        this.memberUuid = memberUuid;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}
