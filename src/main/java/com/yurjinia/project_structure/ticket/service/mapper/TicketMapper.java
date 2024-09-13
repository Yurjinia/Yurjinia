package com.yurjinia.project_structure.ticket.service.mapper;

import com.yurjinia.project_structure.comment.service.mapper.CommentMapper;
import com.yurjinia.project_structure.ticket.dto.CreateTicketRequest;
import com.yurjinia.project_structure.ticket.dto.TicketDTO;
import com.yurjinia.project_structure.ticket.dto.TicketStatusDTO;
import com.yurjinia.project_structure.ticket.entity.TicketEntity;
import com.yurjinia.user.dto.UserProfileDTO;
import com.yurjinia.user.entity.UserEntity;
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
        TicketDTO ticketDTO = new TicketDTO();

        if (ticketEntity.getAssignee() != null) {
            UserEntity assignee = ticketEntity.getAssignee();
            UserProfileDTO assigneeProfileDTO = userProfileMapper.toDto(assignee.getUserProfile());
            ticketDTO.setAssignee(userMapper.toDto(assignee, assigneeProfileDTO));
        }

        if (ticketEntity.getReporter() != null) {
            UserEntity reporter = ticketEntity.getReporter();
            UserProfileDTO reporterProfileDTO = userProfileMapper.toDto(reporter.getUserProfile());
            ticketDTO.setReporter(userMapper.toDto(reporter, reporterProfileDTO));
        }

        if (ticketEntity.getComments() != null) {
            ticketDTO.setComments(commentMapper.toDtos(ticketEntity.getComments()));
        }

        if (ticketEntity.getStatus() != null) {
            ticketDTO.setStatus(TicketStatusDTO.builder().name(ticketEntity.getStatus().getName()).build());

        }

        ticketDTO.setTitle(ticketEntity.getTitle());
        ticketDTO.setType(ticketEntity.getType());
        ticketDTO.setCode(ticketEntity.getCode());
        ticketDTO.setDescription(ticketEntity.getDescription());
        ticketDTO.setStartDate(ticketEntity.getStartDate());
        ticketDTO.setEndDate(ticketEntity.getEndDate());
        ticketDTO.setPosition(ticketEntity.getPosition());
        ticketDTO.setPriority(ticketEntity.getPriority());

        return ticketDTO;

    }
}
