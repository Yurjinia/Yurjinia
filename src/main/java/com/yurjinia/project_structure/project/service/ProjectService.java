package com.yurjinia.project_structure.project.service;

import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.project_structure.project.dto.ProjectDTO;
import com.yurjinia.project_structure.project.entity.ProjectEntity;
import com.yurjinia.project_structure.project.repository.ProjectRepository;
import com.yurjinia.project_structure.project.service.mapper.ProjectMapper;
import com.yurjinia.user.entity.UserEntity;
import com.yurjinia.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final UserService userService;
    private final ProjectMapper projectMapper;
    private final ProjectRepository projectRepository;

    @Transactional
    public ProjectDTO createProject(String userEmail, ProjectDTO projectDTO) {
        UserEntity owner = userService.getByEmail(userEmail);
        validateIfProjectExists(projectDTO);
        validateAllUsersExist(projectDTO.getUsers());

        ProjectEntity projectEntity = projectMapper.toEntity(projectDTO);
        associateUserWithProject(owner, projectEntity);

        inviteUsers(projectDTO);

        return projectDTO;
    }

    private void inviteUsers(ProjectDTO projectDTO) {
        Set<String> users = projectDTO.getUsers();
        String projectName = projectDTO.getName();

        users.forEach(user -> {
            /* ToDo: Refer to next JIRA with having more clarification about the reasons of
                why the code was commented, and when it's going to be uncommented:
                https://pashka1clash.atlassian.net/browse/YUR-114

               inviteUserToTheProject(projectName, ProjectInvitationDTO.builder().email(user).build());
            */
            addUserToProject(user, projectName);
        });
    }

    @Transactional
    public void addUserToProject(String email, String projectName) {
        ProjectEntity projectEntity = projectRepository.findProjectEntityByName(projectName)
                .orElseThrow(() -> new CommonException(ErrorCode.PROJECT_NOT_FOUND, HttpStatus.NOT_FOUND));
        UserEntity userEntity = userService.getByEmail(email);

        associateUserWithProject(userEntity, projectEntity);
    }

    private void validateIfProjectExists(ProjectDTO projectDTO) {
        if (projectRepository.existsByName(projectDTO.getName())) {
            throw new CommonException(ErrorCode.PROJECT_ALREADY_EXISTS, HttpStatus.CONFLICT, List.of("Project by name " + projectDTO.getName() + " already exists"));
        }
    }

    private void associateUserWithProject(UserEntity user, ProjectEntity projectEntity) {
        projectEntity.getUsers().add(user);
        user.getProjects().add(projectEntity);

        projectRepository.save(projectEntity);
        userService.save(user);
    }

    public void validateAllUsersExist(Set<String> userEmails) {
        List<String> existingEmails = userService.findAllByEmail(userEmails).stream().map(UserEntity::getEmail).toList();
        if (existingEmails.size() != userEmails.size()) {
            Set<String> missingEmails = userEmails.stream()
                    .filter(user -> !existingEmails.contains(user))
                    .collect(Collectors.toSet());

            if (!missingEmails.isEmpty()) {
                throw new CommonException(ErrorCode.USER_NOT_FOUND, HttpStatus.CONFLICT,
                        List.of("Users by emails: " + missingEmails + " does not found."));
            }
        }
    }

    /* ToDo: Refer to next JIRA with having more clarification about the reasons of
        why the code was commented, and when it's going to be uncommented:
        https://pashka1clash.atlassian.net/browse/YUR-114

        public void inviteUserToTheProject(String projectName, ProjectInvitationDTO projectInvitationDTO) {
            validateIfProjectNotExists(projectName);
            validateIfUserExistsInProject(projectInvitationDTO.getEmail(), projectName);

            String token = confirmationTokenService.createToken(projectInvitationDTO.getEmail(), projectName);
            String link = "http://localhost:9000/api/v1/projects/confirm?token=" + token; //ToDo: Resolve the security breach (MVP 1.2)
            emailSender.send(projectInvitationDTO.getEmail(), emailService.buildInvitationMessage(link));
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

    */

}
