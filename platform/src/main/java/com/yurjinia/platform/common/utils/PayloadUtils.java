package com.yurjinia.platform.common.utils;

import com.yurjinia.platform.auth.controller.request.RegistrationRequest;
import com.yurjinia.platform.common.content.ContentGeneratorService;
import com.yurjinia.platform.project_structure.board.controller.request.CreateBoardRequest;
import com.yurjinia.platform.project_structure.column.controller.request.CreateColumnRequest;
import com.yurjinia.platform.project_structure.project.dto.CreateProjectRequest;
import lombok.experimental.UtilityClass;

import java.util.HashSet;

@UtilityClass
public class PayloadUtils {

    private final ContentGeneratorService contentGeneratorService = new ContentGeneratorService();

    public CreateProjectRequest createProjectRequest(int projectNumber) {
        CreateProjectRequest createProjectRequest = new CreateProjectRequest();
        createProjectRequest.setProjectCode("PROJ-" + projectNumber);
        createProjectRequest.setProjectName("Project" + projectNumber);
        createProjectRequest.setUserEmails(new HashSet<>());
        return createProjectRequest;
    }

    public CreateBoardRequest createBoardRequest(int boardNumber) {
        return CreateBoardRequest.builder()
                .name("Board " + boardNumber)
                .code("BOARD-" + boardNumber)
                .build();
    }

    public CreateColumnRequest createColumnRequest(String columnName) {
        return CreateColumnRequest.builder()
                .name(columnName).build();
    }

    public RegistrationRequest createRegistrationRequest() {
        String passwordGenerated = contentGeneratorService.generatePassword();
        return RegistrationRequest.builder()
                .firstName(contentGeneratorService.generateFirstName())
                .lastName(contentGeneratorService.generateLastName())
                .username(contentGeneratorService.generateUsername())
                .email(contentGeneratorService.generateEmail())
                .password(passwordGenerated)
                .confirmPassword(passwordGenerated)
                .build();
    }

}
