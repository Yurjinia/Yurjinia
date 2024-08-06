package com.yurjinia.user.service.mapper;

import com.yurjinia.auth.controller.request.RegistrationRequest;
import com.yurjinia.user.dto.UserDTO;
import com.yurjinia.user.dto.UserProfileDTO;
import com.yurjinia.user.entity.UserEntity;
import com.yurjinia.user.entity.UserProfileEntity;
import com.yurjinia.user.enums.UserRole;
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
                .profileDTO(userProfileDTO)
                .build();
    }

}
