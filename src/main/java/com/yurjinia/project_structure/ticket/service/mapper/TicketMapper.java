package com.yurjinia.project_structure.ticket.service.mapper;

import com.yurjinia.project_structure.comment.service.mapper.CommentMapper;
import com.yurjinia.project_structure.ticket.dto.CreateTicketRequest;
import com.yurjinia.project_structure.ticket.dto.TicketDTO;
import com.yurjinia.project_structure.ticket.entity.TicketEntity;
import com.yurjinia.user.service.mapper.UserMapper;
import com.yurjinia.user.service.mapper.UserProfileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketMapper {
    private final UserMapper userMapper;
    private final CommentMapper commentMapper;
    private final UserProfileMapper userProfileMapper;

    public TicketEntity toEntity(CreateTicketRequest createTicketRequest) {
        return TicketEntity.builder()
                .title(createTicketRequest.getName())
                .type(createTicketRequest.getType())
                .build();
    }

    public TicketDTO toDTO(TicketEntity ticketEntity) {
        /*UserEntity assignee = ticketEntity.getAssignee();
        UserEntity reporter = ticketEntity.getReporter();
        UserProfileDTO assigneeProfileDTO = userProfileMapper.toDto(assignee.getUserProfile());
        UserProfileDTO reporterProfileDTO = userProfileMapper.toDto(reporter.getUserProfile());*/
        return TicketDTO.builder()
                .title(ticketEntity.getTitle())
                .type(ticketEntity.getType())
                .code(ticketEntity.getCode())
                .description(ticketEntity.getDescription())
                //.assignee(userMapper.toDto(assignee, assigneeProfileDTO))
                //.comments(commentMapper.toDtos(ticketEntity.getComments()))
                .status(ticketEntity.getStatus())
                .endDate(ticketEntity.getEndDate())
                .startDate(ticketEntity.getStartDate())
                // .reporter(userMapper.toDto(reporter, reporterProfileDTO))
                .priority(ticketEntity.getPriority())
                .build();

    }
}
