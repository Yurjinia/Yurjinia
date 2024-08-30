package com.yurjinia.project_structure.project.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class InviteToProjectRequest {

    @NotEmpty
    private Set<String> userEmails;

}
