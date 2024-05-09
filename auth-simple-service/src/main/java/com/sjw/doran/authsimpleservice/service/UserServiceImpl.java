package com.sjw.doran.authsimpleservice.service;

import com.sjw.doran.authsimpleservice.dto.UserDto;
import com.sjw.doran.authsimpleservice.entity.UserEntity;
import com.sjw.doran.authsimpleservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUserId(username);
        if (userEntity == null)
            throw new UsernameNotFoundException(username + ": not found");

        return new User(userEntity.getUserId(), userEntity.getEncryptedPwd(),
                true, true, true, true, new ArrayList<>());
    }

    @Override
    public UserDto createUser(UserDto userDto) {

        userDto.setUserUuid(UUID.randomUUID().toString());
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(userEntity);
        return userDto;
    }

    @Override
    public List<UserDto> getUsers() {
        List<UserEntity> userEntityList = userRepository.findAll();
        List<UserDto> userDtoList = new ArrayList<>();

        ModelMapper mapper = new ModelMapper();
        userEntityList.forEach(userEntity -> userDtoList.add(mapper.map(userEntity, UserDto.class)));
        return userDtoList;
    }

    @Override
    public UserDto getUserByUserUuid(String userUuid) {

        UserEntity userEntity = userRepository.findByUserUuid(userUuid);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        return userDto;
    }

    @Override
    public UserDto getUserDetailsByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new UsernameNotFoundException(userId);
        }

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(userEntity, UserDto.class);
        return userDto;
    }

    @Override
    public void deleteUser(String userId, String userUuid) {
        UserEntity userEntity = userRepository.findByUserUuid(userUuid);
        if (!userEntity.getUserId().equals(userId)) {
            throw new RuntimeException("아이디가 일치하지 않습니다.");
        }
        userRepository.delete(userEntity);
    }
}
