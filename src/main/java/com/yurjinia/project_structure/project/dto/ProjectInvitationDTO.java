package com.yurjinia.project_structure.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProjectInvitationDTO {
    @NotBlank
    private String email;
}
