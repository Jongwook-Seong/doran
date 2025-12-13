package com.sjw.doran.authservice.mapper;

import com.sjw.doran.authservice.dto.UserDto;
import com.sjw.doran.authservice.entity.UserEntity;
import com.sjw.doran.authservice.vo.UserJoinRequest;
import com.sjw.doran.authservice.vo.UserResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-27T23:39:20+0900",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserEntity toUserEntity(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        UserEntity.UserEntityBuilder userEntity = UserEntity.builder();

        userEntity.userUuid( userDto.getUserUuid() );
        userEntity.username( userDto.getUsername() );
        userEntity.birthDate( userDto.getBirthDate() );
        userEntity.email( userDto.getEmail() );
        userEntity.phoneNumber( userDto.getPhoneNumber() );
        userEntity.role( userDto.getRole() );
        userEntity.userId( userDto.getUserId() );
        userEntity.password( userDto.getPassword() );
        userEntity.activated( userDto.isActivated() );

        return userEntity.build();
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
        userDto.setRole( userEntity.getRole() );
        userDto.setUserId( userEntity.getUserId() );
        userDto.setPassword( userEntity.getPassword() );
        userDto.setActivated( userEntity.isActivated() );
        userDto.setDeleted( userEntity.isDeleted() );

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
        userResponse.setPhoneNumber( userDto.getPhoneNumber() );
        userResponse.setUsername( userDto.getUsername() );

        return userResponse;
    }
}
