package com.sjw.doran.authservice.service;

import com.sjw.doran.authservice.dto.UserDto;
import com.sjw.doran.authservice.entity.User;
import com.sjw.doran.authservice.entity.constant.UserRole;
import com.sjw.doran.authservice.exception.AuthServiceException;
import com.sjw.doran.authservice.exception.ErrorCode;
import com.sjw.doran.authservice.repository.UserRepository;
import com.sjw.doran.authservice.util.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapperUtil modelMapperUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
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

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByUserUuid(String userUuid) {
        User user = userRepository.findByUserUuid(userUuid)
                .orElseThrow(() -> new AuthServiceException(ErrorCode.USER_NOT_FOUND));
        return modelMapperUtil.convertToUserDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByIdentity(String identity) {
        User user = userRepository.findByIdentity(identity)
                .orElseThrow(() -> new AuthServiceException(ErrorCode.USER_NOT_FOUND));
        return modelMapperUtil.convertToUserDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();
        return modelMapperUtil.mapToUserDtoList(users);
    }

    @Override
    @Transactional
    public void modifyUserInfo(String userUuid, String identity, String email, String password) {
        User user = userRepository.findByIdentity(identity).orElseThrow(() ->
                new AuthServiceException(ErrorCode.USER_NOT_FOUND));

        if (!user.getUserUuid().equals(userUuid)) {
            throw new AuthServiceException(ErrorCode.INVALID_USER_UUID);
        }

        user.modifyUserInfo(email, bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(String identity, String userUuid) {
        User user = userRepository.findByIdentity(identity).orElseThrow(() ->
                new AuthServiceException(ErrorCode.USER_NOT_FOUND));

        if (!user.getUserUuid().equals(userUuid)) {
            throw new AuthServiceException(ErrorCode.INVALID_USER_UUID);
        }

        try {
            // memberServiceClient.deleteMember()
            userRepository.delete(user);
        } catch (Exception e) {
            throw new AuthServiceException(ErrorCode.USER_DELETE_ERROR);
        }
    }

    @Override
    @Transactional
    public User userUpdate(User user) {
        return userRepository.save(user);
    }
}
