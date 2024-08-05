package com.yurjinia.project_structure.project.service.mapper;

import com.yurjinia.project_structure.project.dto.CreateProjectRequest;
import com.yurjinia.project_structure.project.dto.ProjectDTO;
import com.yurjinia.project_structure.project.entity.ProjectEntity;
import com.yurjinia.user.entity.UserEntity;
import com.yurjinia.user.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProjectMapper {

    private final UserMapper userMapper;

    public ProjectEntity toEntity(CreateProjectRequest createProjectRequest, UserEntity owner) {
        return ProjectEntity.builder()
                .name(createProjectRequest.getProjectName())
                .code(createProjectRequest.getProjectCode())
                .owner(owner)
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
                .owner(userMapper.toDto(projectEntity.getOwner()))
                .build();
    }

}
