package com.yurjinia.user.service.mapper;

import com.yurjinia.auth.controller.request.RegistrationRequest;
import com.yurjinia.user.dto.UserProfileDTO;
import com.yurjinia.user.entity.UserProfileEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

@Service
public class UserProfileMapper {

    public UserProfileEntity toEntity(RegistrationRequest registrationRequest) {
        return UserProfileEntity.builder()
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .username(registrationRequest.getUsername())
                .build();
    }

    public UserProfileDTO toDto(UserProfileEntity userProfileEntity) {
        return UserProfileDTO.builder()
                .firstName(userProfileEntity.getFirstName())
                .lastName(userProfileEntity.getLastName())
                .username(userProfileEntity.getUsername())
                .avatarId(userProfileEntity.getAvatarId())
                .build();
    }
}
