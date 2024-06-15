package com.yurjinia.user.service;

import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.project_structure.project.entity.ProjectEntity;
import com.yurjinia.user.dto.UserDTO;
import com.yurjinia.user.entity.UserEntity;
import com.yurjinia.user.repository.UserRepository;
import com.yurjinia.user.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return userRepository.findByEmail(email).orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND));
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
            throw new CommonException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

}
