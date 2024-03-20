package com.sjw.doran.authservice.service;

import com.sjw.doran.authservice.dto.UserDto;
import com.sjw.doran.authservice.entity.User;
import com.sjw.doran.authservice.entity.constant.UserRole;
import com.sjw.doran.authservice.repository.UserRepository;
import com.sjw.doran.authservice.util.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapperUtil modelMapperUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void join(UserDto userDto) {
        User user = User.builder()
                .userUuid(UUID.randomUUID().toString())
                .identity(userDto.getIdentity())
                .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .birthDate(userDto.getBirthDate())
                .gender(userDto.getGender())
                .role(UserRole.USER)
                .build();
        userRepository.save(user);
    }
}
