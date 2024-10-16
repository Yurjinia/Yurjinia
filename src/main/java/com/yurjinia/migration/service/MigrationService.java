package com.yurjinia.migration.service;

import com.github.javafaker.Faker;
import com.yurjinia.common.kafka.KafkaProducer;
import com.yurjinia.project_structure.board.controller.request.CreateBoardRequest;
import com.yurjinia.project_structure.board.entity.BoardEntity;
import com.yurjinia.project_structure.board.service.BoardService;
import com.yurjinia.project_structure.column.controller.request.CreateColumnRequest;
import com.yurjinia.project_structure.column.service.ColumnService;
import com.yurjinia.project_structure.comment.contreller.request.CreateCommentRequest;
import com.yurjinia.project_structure.comment.service.CommentService;
import com.yurjinia.project_structure.project.dto.CreateProjectRequest;
import com.yurjinia.project_structure.project.service.ProjectService;
import com.yurjinia.project_structure.ticket.dto.CreateTicketRequest;
import com.yurjinia.project_structure.ticket.service.TicketService;
import com.yurjinia.user.entity.UserEntity;
import com.yurjinia.user.entity.UserProfileEntity;
import com.yurjinia.user.enums.UserRole;
import com.yurjinia.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MigrationService {

    private final UserRepository userRepository;
    private final ProjectService projectService;
    private final BoardService boardService;
    private final ColumnService columnService;
    private final TicketService ticketService;
    private final CommentService commentService;
    private final PasswordEncoder passwordEncoder;
    private final KafkaProducer kafkaProducer;

    //@PostConstruct
    @Transactional
    @KafkaListener(topics = "yurjinia", groupId = "jobs")
    public void createInitialData() {
        System.out.println("Yurjinia Message resive");
        Faker faker = new Faker();
        // Step 1: Create 100 Users and Profiles
        List<UserEntity> users = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            UserProfileEntity profile = UserProfileEntity.builder()
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .username(faker.name().username())
                    .build();

            UserEntity user = UserEntity.builder()
                    .email(faker.internet().emailAddress())
                    .password(passwordEncoder.encode(faker.internet().password())) // Use encoder in real use
                    .role(UserRole.USER)
                    .active(true)
                    .userProfile(profile)
                    .build();

            users.add(user);
        }
        userRepository.saveAll(users);

        // Step 2: Create 10 Projects
        for (int i = 1; i <= 10; i++) {
            CreateProjectRequest createProjectRequest = new CreateProjectRequest();
            createProjectRequest.setProjectCode("PROJ-" + i);
            createProjectRequest.setProjectName("Project" + i);
            createProjectRequest.setUserEmails(new HashSet<>());
            projectService.createProject(users.get(i - 1).getEmail(), createProjectRequest);
            for (int j = 1; j <= 2; j++) {
                CreateBoardRequest createBoardRequest = CreateBoardRequest.builder().name("Board " + j).code("BOARD-" + j).build();
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

                // Step 5: Create 5 Tickets for Each Board (2 for ToDo, 2 for In Progress, 1 for Done)
                createTickets(users.get(i - 1).getEmail(), createProjectRequest.getProjectCode(), createBoardRequest.getCode(), createColumnRequestToDo.getName());
                createTickets(users.get(i - 1).getEmail(), createProjectRequest.getProjectCode(), createBoardRequest.getCode(), createColumnRequestToDo.getName());
                createTickets(users.get(i - 1).getEmail(), createProjectRequest.getProjectCode(), createBoardRequest.getCode(), createColumnRequestInProg.getName());
                createTickets(users.get(i - 1).getEmail(), createProjectRequest.getProjectCode(), createBoardRequest.getCode(), createColumnRequestInProg.getName());
                createTickets(users.get(i - 1).getEmail(), createProjectRequest.getProjectCode(), createBoardRequest.getCode(), createColumnRequestDone.getName());
            }

        }

        kafkaProducer.send("yurjinia", "Done");
        System.out.println("Yurjinia massege send");
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