package com.sjw.doran.authsimpleservice.service;

import com.sjw.doran.authsimpleservice.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);

    List<UserDto> getUsers();

    UserDto getUserByUserUuid(String userUuid);

    UserDto getUserDetailsByUserId(String userId);

    void deleteUser(String userId, String userUuid);
}
