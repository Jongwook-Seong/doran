package com.sjw.doran.authservice.entity;

import com.sjw.doran.authservice.entity.constant.Gender;
import com.sjw.doran.authservice.entity.constant.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    private String userUuid;

    @Column(length = 50)
    private String email;

    @Column(unique = true, length = 20)
    private String identity;

    @Column(length = 100)
    private String password;

    @Column(length = 20)
    private String username;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}
