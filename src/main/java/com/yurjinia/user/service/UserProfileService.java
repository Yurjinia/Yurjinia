package com.yurjinia.user.service;

import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.common.s3.service.AWSS3Service;
import com.yurjinia.user.dto.UserProfileDTO;
import com.yurjinia.user.dto.UsernameDTO;
import com.yurjinia.user.entity.UserEntity;
import com.yurjinia.user.entity.UserProfileEntity;
import com.yurjinia.user.repository.UserProfileRepository;
import com.yurjinia.user.service.mapper.UserProfileMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final AWSS3Service awss3Service;
    private final UserProfileMapper userProfileMapper;
    private final UserProfileRepository userProfileRepository;

    public UserProfileDTO changeUsername(String userEmail, UsernameDTO usernameDTO) {
        validateIfUsernameExists(usernameDTO.getUsername());

        UserProfileEntity userProfileEntity = getUserProfileByEmail(userEmail);

        userProfileEntity.setUsername(usernameDTO.getUsername());
        userProfileRepository.save(userProfileEntity);
        return userProfileMapper.toDto(userProfileEntity);
    }

    public Optional<String> uploadAvatar(UserEntity userEntity, MultipartFile image) {
        return awss3Service.uploadAvatar(userEntity, image);
    }

    public void validateIfUsernameExists(String username) {
        if (userProfileRepository.existsByUsername(username)) {
            throw new CommonException(ErrorCode.USERNAME_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }
    }

    public boolean isUsernameNotEmpty(String username) {
        return StringUtils.isNotEmpty(username);
    }

    public UserProfileDTO getUserProfile(String userEmail) {
        return userProfileMapper.toDto(getUserProfileByEmail(userEmail));
    }

    public UserProfileDTO changeAvatar(String userEmail, MultipartFile image) {
        UserProfileEntity userProfileEntity = getUserProfileByEmail(userEmail);
        awss3Service.changeAvatar(image, userProfileEntity);
        userProfileRepository.save(userProfileEntity);

        return userProfileMapper.toDto(userProfileEntity);
    }

    public UserProfileDTO deleteAvatar(String userEmail) {
        UserProfileEntity userProfileEntity = getUserProfileByEmail(userEmail);
        awss3Service.deleteAvatar(userProfileEntity);
        userProfileRepository.save(userProfileEntity);

        return userProfileMapper.toDto(userProfileEntity);
    }

    private UserProfileEntity getUserProfileByEmail(String email) {
        return userProfileRepository.findByUserEmail(email)
                .orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

}
