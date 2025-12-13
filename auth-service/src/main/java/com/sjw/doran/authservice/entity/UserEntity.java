package com.sjw.doran.authservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
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

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Column(nullable = false, unique = true)
    private String userId;
    @Column(nullable = false, unique = true)
    private String password;

    @JsonIgnore
    @Column(name = "activated", columnDefinition = "boolean default false NOT NULL")
    private boolean activated;
    @Column(name = "is_deleted", columnDefinition = "boolean default false NOT NULL")
    private boolean isDeleted;

    public void modifyPassword(String password) {
        if (password != null && !password.isEmpty()) {
            this.password = password;
        }
    }

    public void changeDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
