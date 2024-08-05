package com.yurjinia.project_structure.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProjectRequest {
    private String projectName;
    private String projectCode;
}
