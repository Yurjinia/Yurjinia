package com.yurjinia.project_structure.project.dto;

import com.yurjinia.user.dto.UserDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    @NotBlank
    private String projectOwnerEmail;

    @NotBlank
    private String projectName;

    @NotBlank
    private String projectCode;

    private Set<String> users;
}
