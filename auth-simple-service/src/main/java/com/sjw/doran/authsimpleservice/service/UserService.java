package com.sjw.doran.authsimpleservice.service;

import com.sjw.doran.authsimpleservice.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);

    UserDto getUserByUserUuid(String userId);
}
