package com.sjw.doran.authservice.service;

import com.sjw.doran.authservice.dto.UserDto;
import com.sjw.doran.authservice.entity.User;
import com.sjw.doran.authservice.repository.UserRepository;
import com.sjw.doran.authservice.util.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapperUtil modelMapperUtil;

    @Override
    public void join(UserDto userDto) {
        User user = modelMapperUtil.convertToEntity(userDto);
        userRepository.save(user);
    }
}
