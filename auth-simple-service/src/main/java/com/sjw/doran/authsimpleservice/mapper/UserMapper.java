package com.sjw.doran.authsimpleservice.mapper;

import com.sjw.doran.authsimpleservice.dto.UserDto;
import com.sjw.doran.authsimpleservice.entity.UserEntity;
import com.sjw.doran.authsimpleservice.vo.UserJoinRequest;
import com.sjw.doran.authsimpleservice.vo.UserResponse;
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
