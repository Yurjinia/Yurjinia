package com.yurjinia.platform.project_structure.project.dto;

import com.yurjinia.platform.project_structure.project.validation.UpperCase;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CreateProjectRequest {

    @NotBlank
    private String projectName;

    @NotBlank
    @UpperCase
    private String projectCode;

    @NotNull
    private Set<String> userEmails;

}
