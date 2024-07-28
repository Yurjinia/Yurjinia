package com.yurjinia.project_structure.project.dto;

import com.yurjinia.project_structure.project.validation.UpperCase;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class ProjectDTO {

    @NotBlank
    private String projectName;

    @NotBlank
    @UpperCase
    private String projectCode;

    private Set<String> userEmails;

}
