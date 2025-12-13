package com.sjw.doran.authservice.repository;

import com.sjw.doran.authservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByUserUuid(String userUuid);

    public Optional<User> findByIdentity(String identity);
}
