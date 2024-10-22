package com.yurjinia.platform.migration.service;

import com.github.javafaker.Faker;
import com.yurjinia.platform.auth.controller.request.RegistrationRequest;
import com.yurjinia.platform.common.kafka.KafkaBrokerService;
import com.yurjinia.platform.project_structure.board.controller.request.CreateBoardRequest;
import com.yurjinia.platform.project_structure.board.entity.BoardEntity;
import com.yurjinia.platform.project_structure.board.service.BoardService;
import com.yurjinia.platform.project_structure.column.controller.request.CreateColumnRequest;
import com.yurjinia.platform.project_structure.column.service.ColumnService;
import com.yurjinia.platform.project_structure.comment.contreller.request.CreateCommentRequest;
import com.yurjinia.platform.project_structure.comment.service.CommentService;
import com.yurjinia.platform.project_structure.project.dto.CreateProjectRequest;
import com.yurjinia.platform.project_structure.project.service.ProjectService;
import com.yurjinia.platform.project_structure.ticket.dto.CreateTicketRequest;
import com.yurjinia.platform.project_structure.ticket.service.TicketService;
import com.yurjinia.platform.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MigrationService {

    private final UserService userService;
    private final ProjectService projectService;
    private final BoardService boardService;
    private final ColumnService columnService;
    private final TicketService ticketService;
    private final CommentService commentService;
    private final KafkaBrokerService kafkaBrokerService;


    @KafkaListener(topics = "yurjinia", groupId = "jobs")
    public void createInitialData() {
        Faker faker = new Faker();
        List<String> users = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            String password = faker.internet().password();
            RegistrationRequest registrationRequest = RegistrationRequest.builder()
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .username(faker.name().username())
                    .email(faker.internet().emailAddress())
                    .password(password)
                    .confirmPassword(password)
                    .build();
            userService.createUser(registrationRequest, "");
            users.add(registrationRequest.getEmail());
        }

        for (int i = 1; i <= 10; i++) {
            CreateProjectRequest createProjectRequest = new CreateProjectRequest();
            createProjectRequest.setProjectCode("PROJ-" + i);
            createProjectRequest.setProjectName("Project" + i);
            createProjectRequest.setUserEmails(new HashSet<>());
            projectService.createProject(users.get(i - 1), createProjectRequest);
            for (int j = 1; j <= 2; j++) {
                CreateBoardRequest createBoardRequest = CreateBoardRequest.builder()
                        .name("Board " + j)
                        .code("BOARD-" + j)
                        .build();
                boardService.createBoard(createBoardRequest, createProjectRequest.getProjectCode());
                CreateColumnRequest createColumnRequestToDo = CreateColumnRequest.builder()
                        .name("ToDo").build();
                columnService.createColumn(createProjectRequest.getProjectCode(), createBoardRequest.getCode(), createColumnRequestToDo);
                CreateColumnRequest createColumnRequestInProg = CreateColumnRequest.builder()
                        .name("In Progress").build();
                columnService.createColumn(createProjectRequest.getProjectCode(), createBoardRequest.getCode(), createColumnRequestInProg);
                CreateColumnRequest createColumnRequestDone = CreateColumnRequest.builder()
                        .name("Done").build();
                columnService.createColumn(createProjectRequest.getProjectCode(), createBoardRequest.getCode(), createColumnRequestDone);

                createTickets(users.get(i - 1), createProjectRequest.getProjectCode(), createBoardRequest.getCode(), createColumnRequestToDo.getName());
                createTickets(users.get(i - 1), createProjectRequest.getProjectCode(), createBoardRequest.getCode(), createColumnRequestToDo.getName());
                createTickets(users.get(i - 1), createProjectRequest.getProjectCode(), createBoardRequest.getCode(), createColumnRequestInProg.getName());
                createTickets(users.get(i - 1), createProjectRequest.getProjectCode(), createBoardRequest.getCode(), createColumnRequestInProg.getName());
                createTickets(users.get(i - 1), createProjectRequest.getProjectCode(), createBoardRequest.getCode(), createColumnRequestDone.getName());
            }

        }

        kafkaBrokerService.send("yurjinia", "Done");
    }

    private void createTickets(String email, String projectCode, String boardCode, String columnName) {
        Faker faker = new Faker();
        CreateTicketRequest createTicketRequest = new CreateTicketRequest();
        createTicketRequest.setTitle(faker.commerce().productName());
        ticketService.createTicket(email, projectCode, boardCode, columnName, createTicketRequest);

        BoardEntity board = boardService.getBoard(boardCode, projectCode);
        CreateCommentRequest createCommentRequest = new CreateCommentRequest();
        createCommentRequest.setText(faker.harryPotter().spell());
        commentService.createComment(email, projectCode, boardCode, (board.getCode() + "-" + board.getUniqueTicketCode()), createCommentRequest);
    }

}