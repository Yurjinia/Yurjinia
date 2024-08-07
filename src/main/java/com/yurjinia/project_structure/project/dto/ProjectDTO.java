package com.yurjinia.project_structure.project.dto;

import com.yurjinia.user.dto.UserDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class ProjectDTO {
    private String projectName;
    private String projectCode;
    private Set<String> userEmails;
    private UserDTO owner;
}
