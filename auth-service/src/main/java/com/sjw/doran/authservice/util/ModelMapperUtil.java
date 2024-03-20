package com.sjw.doran.authservice.util;

import com.sjw.doran.authservice.dto.UserDto;
import com.sjw.doran.authservice.entity.User;
import com.sjw.doran.authservice.vo.request.UserJoinRequest;
import com.sjw.doran.authservice.vo.request.UserLoginRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

@RequiredArgsConstructor
public class ModelMapperUtil {

    private final ModelMapper mapper;

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
}
