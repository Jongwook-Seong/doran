package com.sjw.doran.authservice.service;

import com.sjw.doran.authservice.client.MemberServiceClient;
import com.sjw.doran.authservice.dto.UserDto;
import com.sjw.doran.authservice.entity.UserEntity;
import com.sjw.doran.authservice.entity.UserRole;
import com.sjw.doran.authservice.exception.AuthServiceException;
import com.sjw.doran.authservice.exception.ErrorCode;
import com.sjw.doran.authservice.mapper.UserMapper;
import com.sjw.doran.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final MemberServiceClient memberServiceClient;

    @Override
    public void join(UserDto userDto) {
        UserEntity findUserEntity = userRepository.findByUserId(userDto.getUserId()).orElse(null);
        if (findUserEntity != null) {
            throw new AuthServiceException(ErrorCode.DUPLICATED_USER_ID);
        }

        userDto.setUserUuid(UUID.randomUUID().toString());
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setActivated(true);
        userDto.setRole(UserRole.USER);
        UserEntity userEntity = userMapper.toUserEntity(userDto);

        try {
            memberServiceClient.joinMember(userDto.getUserUuid(), userDto.getUsername());
            userRepository.save(userEntity);
        } catch (Exception e) {
            throw new AuthServiceException(ErrorCode.MEMBER_CREATION_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByUserUuid(String userUuid) {
        UserEntity userEntity = userRepository.findByUserUuid(userUuid).orElseThrow(() ->
                new AuthServiceException(ErrorCode.INVALID_USER_UUID));
        return userMapper.toUserDto(userEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(() ->
                new AuthServiceException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserDto(userEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers() {
        List<UserEntity> userEntityList = userRepository.findAll();
        List<UserDto> userDtoList = new ArrayList<>();
        userEntityList.forEach(userEntity -> userDtoList.add(userMapper.toUserDto(userEntity)));
        return userDtoList;
    }

    @Override
    @Transactional
    public void modifyUserInfo(String userId, String userUuid, String password) {
        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(() ->
                new AuthServiceException(ErrorCode.USER_NOT_FOUND));

        if (!userEntity.getUserUuid().equals(userUuid)) {
            throw new AuthServiceException(ErrorCode.INVALID_USER_UUID);
        }

        userEntity.modifyPassword(passwordEncoder.encode(password));
        userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public void deleteUser(String userId, String userUuid) {
        UserEntity userEntity = userRepository.findByUserUuid(userUuid).orElseThrow(() ->
                new AuthServiceException(ErrorCode.USER_NOT_FOUND));
        if (!userEntity.getUserId().equals(userId)) {
            throw new AuthServiceException(ErrorCode.INVALID_USER_UUID);
        }
        if (userEntity.isDeleted()) {
            throw new AuthServiceException(ErrorCode.USER_NOT_FOUND);
        }

        try {
            memberServiceClient.deleteMember(userEntity.getUserUuid());

            userEntity.changeDeleted(true);
            userRepository.save(userEntity);
        } catch (Exception e) {
            throw new AuthServiceException(ErrorCode.MEMBER_DELETE_ERROR);
        }
    }

    @Override
    @Transactional
    public UserDto userUpdate(UserDto userDto) {
        UserEntity userEntity = userMapper.toUserEntity(userDto);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        return userMapper.toUserDto(savedUserEntity);
    }
}
