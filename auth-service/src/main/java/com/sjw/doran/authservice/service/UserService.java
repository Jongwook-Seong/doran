package com.sjw.doran.authservice.service;

import com.sjw.doran.authservice.dto.UserDto;

import java.util.List;

public interface UserService {

    void join(UserDto userDto);
    UserDto getUserByUserUuid(String userUuid);
    UserDto getUserByUserId(String userId);
    List<UserDto> getUsers();
    void modifyUserInfo(String userId, String userUuid, String password);
    void deleteUser(String userId, String userUuid);
    UserDto userUpdate(UserDto user);
}
