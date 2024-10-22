package com.yurjinia.platform.project_structure.project.dto;

import com.yurjinia.platform.common.validator.EmailValidate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectInvitationDTO {
    @EmailValidate
    private String email;
}
