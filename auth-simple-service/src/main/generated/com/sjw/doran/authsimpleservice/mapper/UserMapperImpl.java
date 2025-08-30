package com.sjw.doran.authsimpleservice.mapper;

import com.sjw.doran.authsimpleservice.dto.UserDto;
import com.sjw.doran.authsimpleservice.entity.UserEntity;
import com.sjw.doran.authsimpleservice.vo.UserJoinRequest;
import com.sjw.doran.authsimpleservice.vo.UserResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-30T23:34:45+0900",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserEntity toUserEntity(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setUserUuid( userDto.getUserUuid() );
        userEntity.setUsername( userDto.getUsername() );
        userEntity.setBirthDate( userDto.getBirthDate() );
        userEntity.setEmail( userDto.getEmail() );
        userEntity.setPhoneNumber( userDto.getPhoneNumber() );
        userEntity.setUserId( userDto.getUserId() );
        userEntity.setEncryptedPwd( userDto.getEncryptedPwd() );

        return userEntity;
    }

    @Override
    public UserDto toUserDto(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setUserUuid( userEntity.getUserUuid() );
        userDto.setUsername( userEntity.getUsername() );
        userDto.setBirthDate( userEntity.getBirthDate() );
        userDto.setEmail( userEntity.getEmail() );
        userDto.setPhoneNumber( userEntity.getPhoneNumber() );
        userDto.setUserId( userEntity.getUserId() );
        userDto.setEncryptedPwd( userEntity.getEncryptedPwd() );

        return userDto;
    }

    @Override
    public UserDto toUserDto(UserJoinRequest request) {
        if ( request == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setUsername( request.getUsername() );
        userDto.setBirthDate( request.getBirthDate() );
        userDto.setEmail( request.getEmail() );
        userDto.setPhoneNumber( request.getPhoneNumber() );
        userDto.setUserId( request.getUserId() );
        userDto.setPassword( request.getPassword() );

        return userDto;
    }

    @Override
    public UserResponse toUserResponse(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        UserResponse userResponse = new UserResponse();

        userResponse.setUserUuid( userDto.getUserUuid() );
        userResponse.setUserId( userDto.getUserId() );
        userResponse.setEmail( userDto.getEmail() );
        userResponse.setUsername( userDto.getUsername() );

        return userResponse;
    }
}
