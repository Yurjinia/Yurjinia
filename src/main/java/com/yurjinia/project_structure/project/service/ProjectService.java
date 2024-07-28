package com.yurjinia.project_structure.project.service;

import com.yurjinia.common.confirmationToken.service.ConfirmationTokenService;
import com.yurjinia.common.emailSender.service.EmailService;
import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.project_structure.project.dto.ProjectDTO;
import com.yurjinia.project_structure.project.entity.ProjectEntity;
import com.yurjinia.project_structure.project.repository.ProjectRepository;
import com.yurjinia.project_structure.project.service.mapper.ProjectMapper;
import com.yurjinia.user.dto.UserDTO;
import com.yurjinia.user.entity.UserEntity;
import com.yurjinia.user.service.UserService;
import com.yurjinia.user.service.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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
        return userService.getByEmail(userEmail)
                .getProjects()
                .stream()
                .map(projectMapper::toDto)
                .toList();
    }

    public List<UserDTO> getProjectUsers(String projectCode) {
        return getProject(projectCode).getUsers().stream()
                .map(userMapper::toDto)
                .toList();
    }

    public ProjectEntity getProject(String projectCode) {
        return projectRepository.findByCode(projectCode)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    @Transactional
    public void createProject(String userEmail, ProjectDTO projectDTO) {
        validateIfProjectNotExists(projectDTO);

        UserEntity owner = userService.getByEmail(userEmail);
        ProjectEntity projectEntity = projectMapper.toEntity(projectDTO);

        projectEntity.setOwner(owner);
        projectEntity.getUsers().add(owner);
        owner.getProjects().add(projectEntity);

        projectRepository.save(projectEntity);
        userService.save(owner);
    }


//    @Transactional
//    public ProjectDTO updateProject(String projectCode, UpdateProjectRequest updateRequest) {
//        // Find existing project by projectCode
//        ProjectEntity existingProject = projectRepository.findByProjectCode(projectCode)
//                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
//
//        // Update project fields if they are not blank in the updateRequest
//        if (StringUtils.isNotBlank(updateRequest.getProjectOwnerEmail())) {
//            existingProject.setProjectOwnerEmail(updateRequest.getProjectOwnerEmail());
//        }
//        if (StringUtils.isNotBlank(updateRequest.getProjectName())) {
//            existingProject.setName(updateRequest.getProjectName());
//        }
//        // Update other fields similarly...
//
//        // Save updated projectEntity
//        ProjectEntity updatedProject = projectRepository.save(existingProject);
//
//        // Map updated ProjectEntity back to ProjectDTO
//        return projectMapper.toDto(updatedProject);
//    }

    private void inviteUsers(ProjectDTO projectDTO) {
        Set<String> users = projectDTO.getUserEmails();
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

    private void validateIfProjectNotExists(ProjectDTO projectDTO) {
        if (projectRepository.existsByNameOrCode(projectDTO.getProjectName(), projectDTO.getProjectCode())) {
            throw new CommonException(ErrorCode.PROJECT_ALREADY_EXISTS, HttpStatus.CONFLICT,
                    List.of("Project by name " + projectDTO.getProjectName() + " or code " + projectDTO.getProjectCode() + " already exists"));
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
