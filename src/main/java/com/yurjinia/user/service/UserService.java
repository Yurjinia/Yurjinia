package com.yurjinia.user.service;

import com.yurjinia.auth.controller.request.RegistrationRequest;
import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.common.security.jwt.service.JwtService;
import com.yurjinia.user.entity.UserEntity;
import com.yurjinia.user.repository.UserRepository;
import com.yurjinia.user.service.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final UserProfileService userProfileService;
    private final JwtService jwtService;

    public void save(RegistrationRequest registrationRequest) {
        save(userMapper.toEntity(registrationRequest));
    }

    public void save(UserEntity userEntity) {
        userEntity.setPassword(jwtService.encode(userEntity.getPassword()));
        userRepository.save(userEntity);
    }

    public UserEntity getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public UserEntity getByUsername(String username) {
        return userRepository.findByUserProfileUsername(username)
                .orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public void createUser(RegistrationRequest registrationRequest, MultipartFile image) {
        validateIfEmailExists(registrationRequest);
        if (userProfileService.isUsernameNotEmpty(registrationRequest.getUsername())) {
            userProfileService.validateIfUsernameExists(registrationRequest.getUsername());
        }

        save(registrationRequest);

        UserEntity userEntity = getByEmail(registrationRequest.getEmail());
        Optional<String> urlOpt = userProfileService.uploadAvatar(userEntity, image);

        if (urlOpt.isPresent()) {
            userEntity.getUserProfile().setAvatarId(urlOpt.get());
            save(userEntity);
        }
    }

    public void createUser(RegistrationRequest registrationRequest, String avatarId) {
        validateIfEmailExists(registrationRequest);
        UserEntity userEntity = userMapper.toEntity(registrationRequest);
        userEntity.getUserProfile().setAvatarId(avatarId);
        save(userEntity);
    }

    public void validateIfEmailExists(RegistrationRequest userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new CommonException(ErrorCode.EMAIL_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Set<UserEntity> findAllByEmail(Set<String> emails) {
        return userRepository.findAllByEmailIn(emails);
    }

    /*
        ToDo: Refer to next JIRA with having more clarification about the reasons of
         why the code was commented, and when it's going to be uncommented:
         https://pashka1clash.atlassian.net/browse/YUR-114

        public void activateUser(String email) {
            UserEntity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

            user.setActive(true);
            userRepository.save(user);
        }

    */

}
