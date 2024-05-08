package com.sjw.doran.authsimpleservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "users")
public class UserEntity extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userUuid;

    @Column(nullable = false, length = 20)
    private String username;
    @Column(nullable = false)
    private Date birthDate;
    @Column(nullable = false, length = 50)
    private String email;
    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String userId;
    @Column(nullable = false, unique = true)
    private String encryptedPwd;
}
