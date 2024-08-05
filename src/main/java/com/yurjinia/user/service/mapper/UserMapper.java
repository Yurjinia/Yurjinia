package com.yurjinia.user.service.mapper;

import com.yurjinia.auth.controller.request.RegistrationRequest;
import com.yurjinia.user.dto.UserDTO;
import com.yurjinia.user.entity.UserEntity;
import com.yurjinia.user.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final UserProfileMapper userProfileMapper;

    public UserEntity toEntity(RegistrationRequest registrationRequest) {
        return UserEntity.builder()
                .email(registrationRequest.getEmail())
                .userProfile(userProfileMapper.toEntity(registrationRequest))
                .active(true) // ToDo: temporary fix, remove when we return activation functionality
                .password(registrationRequest.getPassword())
                .role(UserRole.USER)
                .build();
    }

    public UserDTO toDto(UserEntity userEntity) {
        return UserDTO.builder()
                .email(userEntity.getEmail())
                .profileDTO(userProfileMapper.toDto(userEntity.getUserProfile()))
                .build();
    }

}
