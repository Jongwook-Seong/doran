package com.sjw.doran.authservice.mapper;

import com.sjw.doran.authservice.dto.UserDto;
import com.sjw.doran.authservice.entity.UserEntity;
import com.sjw.doran.authservice.vo.UserJoinRequest;
import com.sjw.doran.authservice.vo.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserEntity toUserEntity(UserDto userDto);
    UserDto toUserDto(UserEntity userEntity);
    UserDto toUserDto(UserJoinRequest request);
    UserResponse toUserResponse(UserDto userDto);
}
