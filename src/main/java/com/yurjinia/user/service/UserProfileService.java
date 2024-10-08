package com.yurjinia.user.service;

import com.yurjinia.auth.controller.request.RegistrationRequest;
import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.common.s3.service.AWSS3Service;
import com.yurjinia.user.dto.UpdateUserProfileRequest;
import com.yurjinia.user.dto.UserProfileDTO;
import com.yurjinia.user.entity.UserEntity;
import com.yurjinia.user.entity.UserProfileEntity;
import com.yurjinia.user.repository.UserProfileRepository;
import com.yurjinia.user.service.mapper.UserProfileMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

import static com.yurjinia.common.application.constants.TextConstants.FORWARD_SLASH;
import static com.yurjinia.user.utils.UserProfileHelper.updateMetaData;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    @Value("${APP.MAIN_PACKAGE}")
    public String mainPackage;

    @Value("${APP.DEFAULT_AVATAR_NAME}")
    public String defaultAvatarName;//ToDO: Implement the adaptation of the image to PNG format

    private final AWSS3Service awss3Service;
    private final UserProfileMapper userProfileMapper;
    private final UserProfileRepository userProfileRepository;

    public Optional<String> uploadAvatar(UserEntity userEntity, MultipartFile image) {
        String key = mainPackage + userEntity.getEmail() + FORWARD_SLASH + defaultAvatarName;

        return awss3Service.uploadImage(image, key);
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

    public void deleteAvatar(String userEmail) {
        UserProfileEntity userProfileEntity = getUserProfileByEmail(userEmail);

        awss3Service.deleteImage(userProfileEntity.getAvatarId());
        userProfileEntity.setAvatarId(null);

        userProfileRepository.save(userProfileEntity);

        userProfileMapper.toDto(userProfileEntity);
    }

    public UserProfileDTO updateUserProfile(String userEmail, MultipartFile image, UpdateUserProfileRequest updateUserProfileRequest) {
        UserProfileEntity userProfileEntity = getUserProfileByEmail(userEmail);

        updateProfileMetaData(userEmail, updateUserProfileRequest, userProfileEntity);
        updateProfileAvatar(userEmail, image);

        userProfileRepository.save(userProfileEntity);
        return mapToDto(userProfileEntity);
    }

    public UserProfileEntity mapToEntity(RegistrationRequest registrationRequest) {
        return userProfileMapper.toEntity(registrationRequest);
    }

    public UserProfileDTO mapToDto(UserProfileEntity userProfileEntity) {
        return userProfileMapper.toDto(userProfileEntity);
    }

    private void updateProfileMetaData(String userEmail, UpdateUserProfileRequest updateUserProfileRequest, UserProfileEntity userProfileEntity) {
        if (updateUserProfileRequest != null) {
            updateMetaData(updateUserProfileRequest.getFirstName(), userProfileEntity::setFirstName);
            updateMetaData(updateUserProfileRequest.getLastName(), userProfileEntity::setLastName);
            changeUsername(userEmail, updateUserProfileRequest.getUsername());
        }
    }

    private void updateProfileAvatar(String userEmail, MultipartFile image) {
        if (Objects.nonNull(image)) {
            updateAvatar(userEmail, image);
        }
    }

    private void changeUsername(String userEmail, String username) {
        validateIfUsernameExists(username);

        UserProfileEntity userProfileEntity = getUserProfileByEmail(userEmail);

        userProfileEntity.setUsername(username);
        userProfileRepository.save(userProfileEntity);
        userProfileMapper.toDto(userProfileEntity);
    }

    private void updateAvatar(String userEmail, MultipartFile image) {
        UserProfileEntity userProfileEntity = getUserProfileByEmail(userEmail);
        UserEntity userEntity = userProfileEntity.getUser();
        String key = mainPackage + userEntity.getEmail() + FORWARD_SLASH + defaultAvatarName;

        String imageURL = awss3Service.uploadFile(image, key);
        userProfileEntity.setAvatarId(imageURL);

        userProfileRepository.save(userProfileEntity);

        userProfileMapper.toDto(userProfileEntity);
    }

    private UserProfileEntity getUserProfileByEmail(String email) {
        return userProfileRepository.findByUserEmail(email)
                .orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

}