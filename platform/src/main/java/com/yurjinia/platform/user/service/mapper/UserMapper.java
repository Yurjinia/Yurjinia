package com.yurjinia.platform.user.service.mapper;

import com.yurjinia.platform.auth.controller.request.RegistrationRequest;
import com.yurjinia.platform.user.entity.UserEntity;
import com.yurjinia.platform.user.enums.UserRole;
import com.yurjinia.platform.user.dto.UserDTO;
import com.yurjinia.platform.user.dto.UserProfileDTO;
import com.yurjinia.platform.user.entity.UserProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public UserEntity toEntity(RegistrationRequest registrationRequest, UserProfileEntity userProfileEntity) {
        return UserEntity.builder()
                .email(registrationRequest.getEmail())
                .userProfile(userProfileEntity)
                .active(true) // ToDo: temporary fix, remove when we return activation functionality
                .password(registrationRequest.getPassword())
                .role(UserRole.USER)
                .build();
    }

    public UserDTO toDto(UserEntity userEntity, UserProfileDTO userProfileDTO) {
        return UserDTO.builder()
                .email(userEntity.getEmail())
                .userProfile(userProfileDTO)
                .build();
    }

}
