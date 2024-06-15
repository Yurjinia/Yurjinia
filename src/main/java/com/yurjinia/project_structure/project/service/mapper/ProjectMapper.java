package com.yurjinia.project_structure.project.service.mapper;

import com.yurjinia.project_structure.project.dto.ProjectDTO;
import com.yurjinia.project_structure.project.entity.ProjectEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectMapper {

    public ProjectEntity toEntity(ProjectDTO projectDTO) {
        return ProjectEntity.builder()
                .name(projectDTO.getName())
                .build();
    }

}
