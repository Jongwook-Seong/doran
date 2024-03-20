package com.sjw.doran.authservice.entity;

import com.sjw.doran.authservice.entity.constant.Gender;
import com.sjw.doran.authservice.entity.constant.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class User extends AuditingFields {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    private String userUuid;

    @Column(unique = true, length = 20)
    @NotBlank
    private String identity;

    @Column(length = 100)
    @NotBlank
    private String password;

    @Column(length = 50)
    private String email;

    @Column(length = 20)
    @NotBlank
    private String username;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotBlank
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @NotBlank
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}
