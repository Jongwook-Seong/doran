package com.sjw.doran.authsimpleservice.repository;

import com.sjw.doran.authsimpleservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUserUuid(String userUuid);
    UserEntity findByUserId(String userId);
}
