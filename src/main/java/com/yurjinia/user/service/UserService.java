package com.yurjinia.user.service;

import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.project_structure.project.entity.ProjectEntity;
import com.yurjinia.user.dto.UserDTO;
import com.yurjinia.user.entity.UserEntity;
import com.yurjinia.user.repository.UserRepository;
import com.yurjinia.user.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public void save(UserDTO userDTO) {
        save(userMapper.toEntity(userDTO));
    }

    public void save(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    public UserEntity getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public UserEntity getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
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

    public void isAuthenticated(UserDTO userDTO) {
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

}
