package com.sjw.doran.authservice.repository;

import com.sjw.doran.authservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserUuid(String userUuid);
    Optional<UserEntity> findByUserId(String userId);
}
