package com.yurjinia.project_structure.project.service;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.yurjinia.common.confirmationToken.service.ConfirmationTokenService;
import com.yurjinia.common.emailSender.service.EmailService;
import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.project_structure.project.dto.ProjectDTO;
import com.yurjinia.project_structure.project.dto.UpdateProjectRequest;
import com.yurjinia.project_structure.project.entity.ProjectEntity;
import com.yurjinia.project_structure.project.repository.ProjectRepository;
import com.yurjinia.project_structure.project.service.mapper.ProjectMapper;
import com.yurjinia.user.dto.UserDTO;
import com.yurjinia.user.entity.UserEntity;
import com.yurjinia.user.service.UserService;
import com.yurjinia.user.service.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final UserService userService;
    private final ProjectMapper projectMapper;
    private final ProjectRepository projectRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;
    private final UserMapper userMapper;

    public List<ProjectDTO> getUserProjects(String userEmail) {
        UserEntity user = userService.getByEmail(userEmail);

        if (user == null) {
            throw new EntityNotFoundException("User with email " + userEmail + " not found");
        }

        List<ProjectEntity> projects = projectRepository.findAllByUsers(user);

        return projects.stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getProjectUsers(String projectCode) {
        ProjectEntity project = projectRepository.findByProjectCode(projectCode)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        List<UserEntity> users = project.getUsers();
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProjectDTO createProject(String userEmail, ProjectDTO projectDTO) {
        validateIfProjectExists(projectDTO);

        // Get owner user by email
        UserEntity owner = userService.getByEmail(userEmail);

        // Set project owner email
        projectDTO.setProjectOwnerEmail(owner.getEmail());

        // Map ProjectDTO to ProjectEntity
        ProjectEntity projectEntity = projectMapper.toEntity(projectDTO);
        projectEntity.setOwner(owner); // Set owner of the project

        // Initialize users list if null
        if (projectEntity.getUsers() == null) {
            projectEntity.setUsers(new ArrayList<>());
        }

        // Add owner to the project's user list
        projectEntity.getUsers().add(owner);

        // Save projectEntity to database
        ProjectEntity savedProject = projectRepository.save(projectEntity);

        // Map ProjectEntity back to ProjectDTO
        return projectMapper.toDto(savedProject);
    }

    @Transactional
    public ProjectDTO updateProject(String projectCode, UpdateProjectRequest updateRequest) {
        // Find existing project by projectCode
        ProjectEntity existingProject = projectRepository.findByProjectCode(projectCode)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        // Update project fields if they are not blank in the updateRequest
        if (StringUtils.isNotBlank(updateRequest.getProjectOwnerEmail())) {
            existingProject.setProjectOwnerEmail(updateRequest.getProjectOwnerEmail());
        }
        if (StringUtils.isNotBlank(updateRequest.getProjectName())) {
            existingProject.setName(updateRequest.getProjectName());
        }
        // Update other fields similarly...

        // Save updated projectEntity
        ProjectEntity updatedProject = projectRepository.save(existingProject);

        // Map updated ProjectEntity back to ProjectDTO
        return projectMapper.toDto(updatedProject);
    }

    private void inviteUsers(ProjectDTO projectDTO) {
        Set<String> users = projectDTO.getUsers();
        String projectName = projectDTO.getProjectName();

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

        projectEntity.getUsers().add(userEntity);
        userEntity.getProjects().add(projectEntity);

        projectRepository.save(projectEntity);
        userService.save(userEntity);
    }

    private void validateIfProjectExists(ProjectDTO projectDTO) {
        if (projectRepository.existsByName(projectDTO.getProjectName())) {
            throw new CommonException(ErrorCode.PROJECT_ALREADY_EXISTS, HttpStatus.CONFLICT, List.of("Project by name " + projectDTO.getProjectName() + " already exists"));
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
