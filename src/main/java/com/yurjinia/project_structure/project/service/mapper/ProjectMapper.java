package com.yurjinia.project_structure.project.service.mapper;

import com.yurjinia.project_structure.project.dto.ProjectDTO;
import com.yurjinia.project_structure.project.entity.ProjectEntity;
import com.yurjinia.user.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProjectMapper {

    public ProjectEntity toEntity(ProjectDTO projectDTO) {
        return ProjectEntity.builder()
                .name(projectDTO.getProjectName())
                .code(projectDTO.getProjectCode())
                .build();
    }

    public ProjectDTO toDto(ProjectEntity projectEntity) {
        Set<String> userEmails = projectEntity.getUsers().stream()
                .map(UserEntity::getEmail)
                .collect(Collectors.toSet());

        return ProjectDTO.builder()
                .projectName(projectEntity.getName())
                .projectCode(projectEntity.getCode())
                .userEmails(userEmails)
                .build();
    }

}
