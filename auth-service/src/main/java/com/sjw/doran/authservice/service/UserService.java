package com.sjw.doran.authservice.service;

import com.sjw.doran.authservice.dto.UserDto;
import com.sjw.doran.authservice.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {

    @Transactional
    void join(UserDto userDto);

    @Transactional(readOnly = true)
    UserDto getUserByUserUuid(String userUuid);

    @Transactional(readOnly = true)
    UserDto getUserByIdentity(String identity);

    @Transactional(readOnly = true)
    List<UserDto> getUsers();

    @Transactional
    void modifyUserInfo(String userUuid, String identity, String email, String password);

    @Transactional
    void deleteUser(String identity, String userUuid);

    @Transactional
    User userUpdate(User user);
}
