package com.sjw.doran.authservice.service;

import com.sjw.doran.authservice.dto.UserDto;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    @Transactional
    void join(UserDto userDto);

    @Transactional(readOnly = true)
    UserDto getUserByIdentity(String identity);
}
