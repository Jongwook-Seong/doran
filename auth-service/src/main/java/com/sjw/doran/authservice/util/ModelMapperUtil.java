package com.sjw.doran.authservice.util;

import com.sjw.doran.authservice.dto.UserDto;
import com.sjw.doran.authservice.entity.User;
import com.sjw.doran.authservice.vo.request.UserJoinRequest;
import com.sjw.doran.authservice.vo.request.UserLoginRequest;
import com.sjw.doran.authservice.vo.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ModelMapperUtil {

    private final ModelMapper mapper;

    public UserDto convertToUserDto(User user) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(user, UserDto.class);
    }

    public UserDto convertToUserDto(UserJoinRequest request) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(request, UserDto.class);
    }

    public UserDto convertToUserDto(UserLoginRequest request) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(request, UserDto.class);
    }

    public User convertToEntity(UserDto userDto) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(userDto, User.class);
    }

    public UserResponse convertToUserResponse(UserDto userDto) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(userDto, UserResponse.class);
    }

    public List<UserDto> mapToUserDtoList(List<User> userList) {
        List<UserDto> userDtoList = userList.stream()
                .map(this::convertToUserDto)
                .collect(Collectors.toList());
        return userDtoList;
    }

    public List<UserResponse> mapToUserResponseList(List<UserDto> userDtoList) {
        List<UserResponse> userResponseList = userDtoList.stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
        return userResponseList;
    }
}
