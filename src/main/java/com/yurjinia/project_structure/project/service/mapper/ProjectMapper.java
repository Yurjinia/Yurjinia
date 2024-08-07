package com.yurjinia.project_structure.project.service.mapper;

import com.yurjinia.project_structure.project.dto.CreateProjectRequest;
import com.yurjinia.project_structure.project.dto.ProjectDTO;
import com.yurjinia.project_structure.project.entity.ProjectEntity;
import com.yurjinia.user.dto.UserDTO;
import com.yurjinia.user.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProjectMapper {

    public ProjectEntity toEntity(CreateProjectRequest createProjectRequest, UserEntity owner) {
        return ProjectEntity.builder()
                .name(createProjectRequest.getProjectName())
                .code(createProjectRequest.getProjectCode())
                .owner(owner)
                .build();
    }

    public ProjectDTO toDto(ProjectEntity projectEntity, UserDTO ownerDto) {
        Set<String> userEmails = projectEntity.getUsers().stream()
                .map(UserEntity::getEmail)
                .collect(Collectors.toSet());

        return ProjectDTO.builder()
                .projectName(projectEntity.getName())
                .projectCode(projectEntity.getCode())
                .userEmails(userEmails)
                .owner(ownerDto)
                .build();
    }

}
