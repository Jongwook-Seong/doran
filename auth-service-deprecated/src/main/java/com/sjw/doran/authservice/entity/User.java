package com.sjw.doran.authservice.entity;

import com.sjw.doran.authservice.entity.constant.Gender;
import com.sjw.doran.authservice.entity.constant.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Data
@Builder
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
    @NotNull
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @NotNull
    private UserRole role;

    public void modifyUserInfo(String email, String password) {
        if (email != null)
            this.email = email;

        if (password != null)
            this.password = password;
    }
}
