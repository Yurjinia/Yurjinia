package com.yurjinia.platform.common.utils;

import com.yurjinia.platform.auth.controller.request.RegistrationRequest;
import com.yurjinia.platform.project_structure.board.controller.request.CreateBoardRequest;
import com.yurjinia.platform.project_structure.column.controller.request.CreateColumnRequest;
import com.yurjinia.platform.project_structure.comment.contreller.request.CreateCommentRequest;
import com.yurjinia.platform.project_structure.project.dto.CreateProjectRequest;
import com.yurjinia.platform.project_structure.ticket.dto.CreateTicketRequest;
import com.yurjinia.platform.project_structure.ticket.entity.TicketType;
import lombok.experimental.UtilityClass;

import java.util.HashSet;

@UtilityClass
public class PayloadUtils {

    public static CreateProjectRequest createProjectRequest(int projectNumber) {
        CreateProjectRequest createProjectRequest = new CreateProjectRequest();
        createProjectRequest.setProjectCode("PROJ-" + projectNumber);
        createProjectRequest.setProjectName("Project" + projectNumber);
        createProjectRequest.setUserEmails(new HashSet<>());
        return createProjectRequest;
    }

    public static CreateBoardRequest createBoardRequest(int boardNumber) {
        return CreateBoardRequest.builder()
                .name("Board " + boardNumber)
                .code("BOARD-" + boardNumber)
                .build();
    }

    public static CreateColumnRequest createColumnRequest(String columnName) {
        return CreateColumnRequest.builder().name(columnName).build();
    }

    public static CreateTicketRequest createTicketRequest() {
        CreateTicketRequest createTicketRequest = new CreateTicketRequest();
        createTicketRequest.setTitle(ContentGeneratorUtils.generateTitle());
        createTicketRequest.setType(TicketType.TASK);
        return createTicketRequest;
    }

    public static CreateCommentRequest createCommentRequest() {
        CreateCommentRequest createCommentRequest = new CreateCommentRequest();
        createCommentRequest.setText(ContentGeneratorUtils.generateComment());
        return createCommentRequest;
    }

    public static RegistrationRequest createRegistrationRequest() {
        String passwordGenerated = ContentGeneratorUtils.generatePassword();
        return RegistrationRequest.builder()
                .firstName(ContentGeneratorUtils.generateFirstName())
                .lastName(ContentGeneratorUtils.generateLastName())
                .username(ContentGeneratorUtils.generateUsername())
                .email(ContentGeneratorUtils.generateEmail())
                .password(passwordGenerated)
                .confirmPassword(passwordGenerated)
                .build();
    }

}
