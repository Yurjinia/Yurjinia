package com.yurjinia.platform.migration.service;

import com.yurjinia.platform.auth.controller.request.RegistrationRequest;
import com.yurjinia.platform.common.content.ContentGeneratorService;
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
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    private final ContentGeneratorService contentGeneratorService;

    @KafkaListener(topics = "yurjinia", groupId = "jobs")
    public void createInitialData(JobStatus jobStatus) {
        if (!jobStatus.equals(JobStatus.INIT)) {
            return;
        }
        System.out.println("Message resive:jobStatus: " + jobStatus);
        List<String> users = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            RegistrationRequest registrationRequest = PayloadUtils.createRegistrationRequest();

            userService.createUser(registrationRequest, "");
            users.add(registrationRequest.getEmail());
        }

        for (int i = 1; i <= 10; i++) {
            String projectCode = createProject(i, users.get(i - 1));

            for (int j = 1; j <= 2; j++) {
                String boardCode = createBoard(j, projectCode);

                String toDo = createColumn("ToDo", projectCode, boardCode);
                String inProgress = createColumn("In Progress", projectCode, boardCode);
                String done = createColumn("Done", projectCode, boardCode);

                createComments(users.get(i - 1), projectCode, boardCode, List.of(toDo, inProgress, done));

            }
        }

        kafkaBrokerService.send(KafkaConstants.TOPIC, JobStatus.DONE);
        System.out.println("Message send");
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

    private void createComments(String userEmail, String projectCode, String boardCode, List<String> columnName) {
        createTickets(userEmail, projectCode, boardCode, columnName.getFirst());
        createTickets(userEmail, projectCode, boardCode, columnName.getFirst());
        createTickets(userEmail, projectCode, boardCode, columnName.get(1));
        createTickets(userEmail, projectCode, boardCode, columnName.get(1));
        createTickets(userEmail, projectCode, boardCode, columnName.get(2));
    }

    private void createTickets(String email, String projectCode, String boardCode, String columnName) {
        CreateTicketRequest createTicketRequest = new CreateTicketRequest();
        createTicketRequest.setTitle(contentGeneratorService.generateTitle());
        ticketService.createTicket(email, projectCode, boardCode, columnName, createTicketRequest);

        BoardEntity board = boardService.getBoard(boardCode, projectCode);
        CreateCommentRequest createCommentRequest = new CreateCommentRequest();
        createCommentRequest.setText(contentGeneratorService.generateComment());
        commentService.createComment(email, projectCode, boardCode, (board.getCode() + "-" + board.getUniqueTicketCode()), createCommentRequest);
    }

}