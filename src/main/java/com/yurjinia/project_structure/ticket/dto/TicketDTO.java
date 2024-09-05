package com.yurjinia.project_structure.ticket.dto;

import com.yurjinia.project_structure.comment.dto.CommentDTO;
import com.yurjinia.project_structure.ticket.entity.TicketPriority;
import com.yurjinia.project_structure.ticket.entity.TicketStatusEntity;
import com.yurjinia.project_structure.ticket.entity.TicketType;
import com.yurjinia.user.dto.UserDTO;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {

    private String title;

    @Enumerated(EnumType.STRING)
    private TicketType type;

    private String code;

    private TicketStatusEntity status;


    private String description;

    private List<CommentDTO> comments;

    private LocalDate startDate;

    private LocalDate endDate;

    private UserDTO assignee;

    private UserDTO reporter;

    private TicketPriority priority;

    private Long position;

    private LocalDate created;

    private LocalDate updated;
}
