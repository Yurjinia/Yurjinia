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

import static com.yurjinia.common.s3.constants.AWSS3Constants.DEFAULT_AVATAR_NAME;
import static com.yurjinia.common.s3.constants.AWSS3Constants.FORWARD_SLASH;
import static com.yurjinia.common.s3.constants.AWSS3Constants.MAIN_PACKAGE;

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
        String key = MAIN_PACKAGE + userEntity.getEmail() + FORWARD_SLASH + DEFAULT_AVATAR_NAME;

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

    public UserProfileDTO updateAvatar(String userEmail, MultipartFile image) {
        UserProfileEntity userProfileEntity = getUserProfileByEmail(userEmail);
        UserEntity userEntity = userProfileEntity.getUser();
        String key = MAIN_PACKAGE + userEntity.getEmail() + FORWARD_SLASH + DEFAULT_AVATAR_NAME;

        String imageURL = awss3Service.uploadFile(image, key);
        userProfileEntity.setAvatarId(imageURL);

        userProfileRepository.save(userProfileEntity);

        return userProfileMapper.toDto(userProfileEntity);
    }

    public UserProfileDTO deleteAvatar(String userEmail) {
        UserProfileEntity userProfileEntity = getUserProfileByEmail(userEmail);

        awss3Service.deleteImage(userProfileEntity.getAvatarId());
        userProfileEntity.setAvatarId(null);

        userProfileRepository.save(userProfileEntity);

        return userProfileMapper.toDto(userProfileEntity);
    }

    private UserProfileEntity getUserProfileByEmail(String email) {
        return userProfileRepository.findByUserEmail(email)
                .orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

}
