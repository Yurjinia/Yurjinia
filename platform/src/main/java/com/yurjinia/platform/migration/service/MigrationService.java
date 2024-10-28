package com.yurjinia.platform.migration.service;

import com.yurjinia.platform.auth.controller.request.RegistrationRequest;
import com.yurjinia.platform.common.kafka.KafkaBrokerService;
import com.yurjinia.platform.common.kafka.constant.KafkaConstants;
import com.yurjinia.platform.common.utils.PayloadUtils;
import com.yurjinia.platform.migration.enums.JobStatus;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class MigrationService {

    private final UserService userService;
    private final BoardService boardService;
    private final ColumnService columnService;
    private final TicketService ticketService;
    private final ProjectService projectService;
    private final CommentService commentService;
    private final KafkaBrokerService kafkaBrokerService;

    @KafkaListener(topics = "yurjinia", groupId = "jobs")
    public void createInitialData(JobStatus jobStatus) {
        if (!jobStatus.equals(JobStatus.INIT)) {
            return;
        }
        log.info("Message received: {}", jobStatus);
        List<String> users = new ArrayList<>();

        IntStream.range(1, 101).forEach(i -> {
            RegistrationRequest registrationRequest = PayloadUtils.createRegistrationRequest();

            userService.createUser(registrationRequest, "");
            users.add(registrationRequest.getEmail());
        });

        IntStream.range(1, 11).forEach(i -> {
            String projectCode = createProject(i, users.get(i - 1));
            IntStream.range(1, 3).forEach(j -> {
                String boardCode = createBoard(j, projectCode);

                String toDo = createColumn("ToDo", projectCode, boardCode);
                String inProgress = createColumn("In Progress", projectCode, boardCode);
                String done = createColumn("Done", projectCode, boardCode);

                createTicketsWithComment(users.get(i - 1), projectCode, boardCode, List.of(toDo, inProgress, done));
            });
        });

        kafkaBrokerService.send(KafkaConstants.TOPIC, JobStatus.DONE);
        log.info("Message sent: {}", jobStatus);
    }

    private String createProject(int projectNumber, String userEmail) {
        CreateProjectRequest createProjectRequest = PayloadUtils.createProjectRequest(projectNumber);

        projectService.createProject(userEmail, createProjectRequest);
        return createProjectRequest.getProjectCode();
    }

    private String createBoard(int boardNumber, String projectCode) {
        CreateBoardRequest createBoardRequest = PayloadUtils.createBoardRequest(boardNumber);

        boardService.createBoard(createBoardRequest, projectCode);
        return createBoardRequest.getCode();
    }

    private String createColumn(String columnName, String projectCode, String boardCode) {
        CreateColumnRequest createColumnRequest = PayloadUtils.createColumnRequest(columnName);
        columnService.createColumn(projectCode, boardCode, createColumnRequest);
        return columnName;
    }

    private void createTicketsWithComment(String userEmail, String projectCode, String boardCode, List<String> columnName) {
        createTicketWithComment(userEmail, projectCode, boardCode, columnName.getFirst());
        createTicketWithComment(userEmail, projectCode, boardCode, columnName.getFirst());
        createTicketWithComment(userEmail, projectCode, boardCode, columnName.get(1));
        createTicketWithComment(userEmail, projectCode, boardCode, columnName.get(1));
        createTicketWithComment(userEmail, projectCode, boardCode, columnName.get(2));
    }

    private void createTicketWithComment(String email, String projectCode, String boardCode, String columnName) {
        CreateTicketRequest createTicketRequest = PayloadUtils.createTicketRequest();
        ticketService.createTicket(email, projectCode, boardCode, columnName, createTicketRequest);

        createComment(email, projectCode, boardCode);
    }

    private void createComment(String email, String projectCode, String boardCode) {
        BoardEntity board = boardService.getBoard(boardCode, projectCode);
        CreateCommentRequest createCommentRequest = PayloadUtils.createCommentRequest();
        commentService.createComment(email, projectCode, boardCode, (board.getCode() + "-" + board.getUniqueTicketCode()), createCommentRequest);
    }

}