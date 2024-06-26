package com.yurjinia.project_structure.project.service;

import com.yurjinia.common.emailSender.EmailSender;
import com.yurjinia.common.emailSender.service.EmailService;
import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.common.confirmationToken.entity.ConfirmationTokenEntity;
import com.yurjinia.common.confirmationToken.service.ConfirmationTokenService;
import com.yurjinia.project_structure.project.dto.ProjectDTO;
import com.yurjinia.project_structure.project.dto.ProjectInvitationDTO;
import com.yurjinia.project_structure.project.entity.ProjectEntity;
import com.yurjinia.project_structure.project.repository.ProjectRepository;
import com.yurjinia.project_structure.project.service.mapper.ProjectMapper;
import com.yurjinia.user.entity.UserEntity;
import com.yurjinia.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final EmailSender emailSender;
    private final UserService userService;
    private final ProjectMapper projectMapper;
    private final ProjectRepository projectRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;

    @Transactional
    public ProjectDTO createProject(String userEmail, ProjectDTO projectDTO) {
        validateIfProjectExists(projectDTO);
        userService.validateIfUsersExists(projectDTO.getUsers().stream().toList());

        ProjectEntity projectEntity = projectMapper.toEntity(projectDTO);
        UserEntity userEntityList = userService.getByEmail(userEmail);
        projectEntity.setUsers(List.of(userEntityList));
        projectRepository.save(projectEntity);

        userService.addProject(projectEntity);

        inviteUsers(projectDTO);

        return projectDTO;
    }

    private void inviteUsers(ProjectDTO projectDTO) {
        Set<String> users = projectDTO.getUsers();
        String projectName = projectDTO.getName();

        users.forEach(user -> {
            inviteUserToTheProject(projectName, ProjectInvitationDTO.builder().email(user).build());
        });
    }

    public void inviteUserToTheProject(String projectName, ProjectInvitationDTO projectInvitationDTO) {
        validateIfProjectNotExists(projectName);
        validateIfUserExistsInProject(projectInvitationDTO.getEmail(), projectName);

        String token = confirmationTokenService.createToken(projectInvitationDTO.getEmail(), projectName);
        String link = "http://localhost:9000/api/v1/projects/confirm?token=" + token;//ToDo: Resolve the security breach (MVP 1.2)
        emailSender.send(projectInvitationDTO.getEmail(), emailService.buildInvitationMessage(link));
    }

    @Transactional
    public void addUserToProject(String email, String projectName) {
        ProjectEntity projectEntity = projectRepository.findProjectEntityByName(projectName)
                .orElseThrow(() -> new CommonException(ErrorCode.PROJECT_NOT_FOUND, HttpStatus.NOT_FOUND));
        UserEntity userEntity = userService.getByEmail(email);

        projectEntity.getUsers().add(userEntity);
        userEntity.getProjects().add(projectEntity);

        projectRepository.save(projectEntity);
        userService.save(userEntity);
    }

    @Transactional
    public void confirmInvite(String token) {
        ConfirmationTokenEntity confirmationToken = confirmationTokenService.getToken(token);

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new CommonException(ErrorCode.TOKEN_EXPIRED, HttpStatus.GATEWAY_TIMEOUT);
        }

        addUserToProject(confirmationToken.getUserEmail(), confirmationToken.getProjectName());
        confirmationTokenService.deleteToken(token);
    }

    private void validateIfProjectExists(ProjectDTO projectDTO) {
        if (projectRepository.existsByName(projectDTO.getName())) {
            throw new CommonException(ErrorCode.PROJECT_ALREADY_EXISTS, HttpStatus.CONFLICT, List.of("Project by name " + projectDTO.getName() + " already exists"));
        }
    }

    private void validateIfProjectNotExists(String projectName) {
        if (!projectRepository.existsByName(projectName)) {
            throw new CommonException(ErrorCode.PROJECT_NOT_FOUND, HttpStatus.NOT_FOUND, List.of("Project by name " + projectName + " already exists"));
        }
    }

    private void validateIfUserExistsInProject(String email, String projectName) {
        ProjectEntity projectEntity = projectRepository.findProjectEntityByName(projectName).
                orElseThrow(() -> new CommonException(ErrorCode.PROJECT_NOT_FOUND, HttpStatus.NOT_FOUND));

        List<String> emails = projectEntity.getUsers().stream().map(UserEntity::getEmail).toList();
        if (emails.contains(email)) {
            throw new CommonException(ErrorCode.USER_ALREADY_IN_PROJECT, HttpStatus.CONFLICT);
        }

    }

}
