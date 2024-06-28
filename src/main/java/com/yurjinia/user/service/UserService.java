package com.yurjinia.user.service;

import com.yurjinia.auth.controller.request.RegistrationRequest;
import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.project_structure.project.entity.ProjectEntity;
import com.yurjinia.user.entity.UserEntity;
import com.yurjinia.user.repository.UserRepository;
import com.yurjinia.user.service.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final UserProfileService userProfileService;

    public void save(RegistrationRequest registrationRequest) {
        save(userMapper.toEntity(registrationRequest));
    }

    public void save(UserEntity userEntity) {
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

    public UserEntity findByEmail(String email) {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(email);
        return optionalUser.orElse(null);
    }

    public List<UserEntity> getAllByEmails(List<String> emails) {
        return userRepository.findAllByEmailIn(emails);
    }

    public void addProject(ProjectEntity projectEntity) {
        UserEntity userEntity = projectEntity.getUsers().getFirst();

        if (userEntity.getProjects() == null) {
            userEntity.setProjects(List.of(projectEntity));
        } else {
            userEntity.getProjects().add(projectEntity);
        }

        save(userEntity);
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

    public void validateIfUsersExists(List<String> userEmails) {
        List<String> emailsFromDB = userRepository.findAllByEmailIn(userEmails).stream().map(UserEntity::getEmail).toList();
        if (emailsFromDB.size() != userEmails.size()) {
            Set<String> missingUsers = userEmails.stream()
                    .filter(user -> !emailsFromDB.contains(user))
                    .collect(Collectors.toSet());

            if (!missingUsers.isEmpty()) {
                throw new CommonException(ErrorCode.USER_NOT_FOUND, HttpStatus.CONFLICT,
                        List.of("Users by emails: " + missingUsers + " does not found."));
            }
        }
    }

    public void activateUser(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        user.setActive(true);
        userRepository.save(user);
    }

}
