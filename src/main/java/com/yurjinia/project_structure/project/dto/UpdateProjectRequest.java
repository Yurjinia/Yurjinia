package com.yurjinia.project_structure.project.dto;

import com.yurjinia.project_structure.project.validation.UpperCase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProjectRequest {

    @UpperCase
    private String projectCode;

    private String projectName;

}
