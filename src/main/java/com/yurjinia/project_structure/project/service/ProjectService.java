package com.yurjinia.project_structure.project.service;

import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.project_structure.project.dto.CreateProjectRequest;
import com.yurjinia.project_structure.project.dto.InviteToProjectRequest;
import com.yurjinia.project_structure.project.dto.ProjectDTO;
import com.yurjinia.project_structure.project.dto.UpdateProjectRequest;
import com.yurjinia.project_structure.project.entity.ProjectEntity;
import com.yurjinia.project_structure.project.repository.ProjectRepository;
import com.yurjinia.project_structure.project.service.mapper.ProjectMapper;
import com.yurjinia.user.dto.UserDTO;
import com.yurjinia.user.entity.UserEntity;
import com.yurjinia.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.yurjinia.common.exception.ErrorCode.PROJECT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final UserService userService;
    private final ProjectMapper projectMapper;
    private final ProjectRepository projectRepository;

    public List<ProjectDTO> getUserProjects(String userEmail) {
        UserEntity user = userService.getByEmail(userEmail);
        UserDTO ownerDto = userService.mapToDto(user);

        return user.getProjects()
                .stream()
                .map(projectEntity -> projectMapper.toDto(projectEntity, ownerDto))
                .toList();
    }

    public List<UserDTO> getProjectUsers(String projectCode) {
        Set<UserEntity> users = getProject(projectCode).getUsers();
        return userService.mapToDto(users);
    }

    public ProjectEntity getProject(String projectCode) {
        return projectRepository.findByCode(projectCode)
                .orElseThrow(() -> new CommonException(ErrorCode.PROJECT_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @Transactional
    public void createProject(String userEmail, CreateProjectRequest createProjectRequest) {
        validateIfProjectNotExists(createProjectRequest);
        validateAllUsersExist(createProjectRequest.getUserEmails());
        validateOwnerNotInUserList(userEmail, createProjectRequest.getUserEmails());

        UserEntity owner = userService.getByEmail(userEmail);
        ProjectEntity projectEntity = projectMapper.toEntity(createProjectRequest, owner);

        if (projectEntity.getUsers().contains(owner)) {
            throw new CommonException(ErrorCode.USER_ALREADY_IN_PROJECT, HttpStatus.CONFLICT, List.of("User " + owner.getEmail() + " is already in the project"));
        }
        associateUserWithProject(owner, projectEntity);

        inviteUsers(createProjectRequest.getProjectCode(), createProjectRequest.getUserEmails());
    }

    @Transactional
    public ProjectDTO updateProject(String projectCode, UpdateProjectRequest updateProjectRequest) {
        ProjectEntity existingProject = findProjectByCode(projectCode);

        validateIfProjectNotConflicts(updateProjectRequest, projectCode);

        if (StringUtils.isNotBlank(updateProjectRequest.getProjectName())) {
            existingProject.setName(updateProjectRequest.getProjectName());
        }

        if (StringUtils.isNotBlank(updateProjectRequest.getProjectCode())) {
            existingProject.setCode(updateProjectRequest.getProjectCode());
        }

        ProjectEntity updatedProject = projectRepository.save(existingProject);

        UserDTO ownerDto = userService.mapToDto(updatedProject.getOwner());
        return projectMapper.toDto(updatedProject, ownerDto);
    }

    @Transactional
    public void deleteProject(String projectCode) {
        ProjectEntity project = getProject(projectCode);
        projectRepository.delete(project);
    }

    @Transactional
    public void deleteUserFromProject(String projectCode, String userEmail) {
        ProjectEntity project = getProject(projectCode);

        UserEntity user = userService.getByEmail(userEmail);

        if (!project.getUsers().contains(user)) {
            throw new CommonException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND, List.of("User is not part of this project"));
        }

        if (project.getOwner().equals(user)) {
            throw new CommonException(ErrorCode.OWNER_CANNOT_BE_REMOVED, HttpStatus.CONFLICT, List.of("Owner cannot be removed from the project"));
        }

        project.getUsers().remove(user);
        user.getProjects().remove(project);

        projectRepository.save(project);
        userService.save(user);
    }

    @Transactional
    public void inviteUsers(String projectCode, InviteToProjectRequest inviteToProjectRequest) {
        Set<String> userEmails = inviteToProjectRequest.getUserEmails();

        inviteUsers(projectCode, userEmails);
    }

    private void inviteUsers(String projectCode, Set<String> userEmails) {
        validateAllUsersExist(userEmails);

        userEmails.forEach(userEmail -> addUserToProject(userEmail, projectCode));
    }

    @Transactional
    public void addUserToProject(String email, String projectCode) {
        ProjectEntity projectEntity = projectRepository.findByCode(projectCode)
                .orElseThrow(() -> new CommonException(PROJECT_NOT_FOUND, HttpStatus.NOT_FOUND, List.of("Project not found with code: " + projectCode)));
        UserEntity userEntity = userService.getByEmail(email);
        associateUserWithProject(userEntity, projectEntity);
    }

    private void associateUserWithProject(UserEntity user, ProjectEntity projectEntity) {
        projectEntity.getUsers().add(user);
        user.getProjects().add(projectEntity);

        projectRepository.save(projectEntity);
        userService.save(user);
    }

    private ProjectEntity findProjectByCode(String projectCode) {
        return projectRepository.findByCode(projectCode)
                .orElseThrow(() -> new CommonException(PROJECT_NOT_FOUND, HttpStatus.NOT_FOUND, List.of("Project not found with code: " + projectCode)));
    }

    private void validateIfProjectNotConflicts(UpdateProjectRequest updateProjectRequest, String existingProjectCode) {
        String newProjectCode = updateProjectRequest.getProjectCode();
        if (StringUtils.isNotBlank(newProjectCode) && !newProjectCode.equals(existingProjectCode)
                && projectRepository.existsByCode(newProjectCode)) {
            throw new CommonException(ErrorCode.PROJECT_ALREADY_EXISTS, HttpStatus.CONFLICT,
                    List.of("Project by code " + newProjectCode + " already exists"));
        }

        String newProjectName = updateProjectRequest.getProjectName();
        if (StringUtils.isNotBlank(newProjectName) && projectRepository.existsByName(newProjectName)) {
            throw new CommonException(ErrorCode.PROJECT_ALREADY_EXISTS, HttpStatus.CONFLICT,
                    List.of("Project by name " + newProjectName + " already exists"));
        }
    }

    private void validateOwnerNotInUserList(String ownerEmail, Set<String> userEmails) {
        if (userEmails != null && userEmails.contains(ownerEmail)) {
            throw new CommonException(ErrorCode.OWNER_CANNOT_BE_A_USER, HttpStatus.BAD_REQUEST,
                    List.of("The project owner cannot be included in the list of users"));
        }
    }

    private void validateIfProjectNotExists(CreateProjectRequest createProjectRequest) {
        if (projectRepository.existsByNameOrCode(createProjectRequest.getProjectName(), createProjectRequest.getProjectCode())) {
            throw new CommonException(ErrorCode.PROJECT_ALREADY_EXISTS, HttpStatus.CONFLICT,
                    List.of("Project by name " + createProjectRequest.getProjectName() + " or code " + createProjectRequest.getProjectCode() + " already exists"));
        }
    }

    private void validateAllUsersExist(Set<String> userEmails) {
        List<String> existingEmails = userService.findAllByEmail(userEmails).stream().map(UserEntity::getEmail).toList();
        if (existingEmails.size() != userEmails.size()) {
            Set<String> missingEmails = userEmails.stream()
                    .filter(email -> !existingEmails.contains(email))
                    .collect(Collectors.toSet());

            if (!missingEmails.isEmpty()) {
                throw new CommonException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND,
                        List.of("Users with emails " + missingEmails + " were not found."));
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
