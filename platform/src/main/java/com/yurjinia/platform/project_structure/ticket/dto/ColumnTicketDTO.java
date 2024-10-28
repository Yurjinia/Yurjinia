package com.yurjinia.platform.project_structure.ticket.dto;

import com.yurjinia.platform.project_structure.ticket.entity.TicketPriority;
import com.yurjinia.platform.project_structure.ticket.entity.TicketType;
import com.yurjinia.platform.user.dto.UserDTO;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnTicketDTO {

    private String title;

    @Enumerated(EnumType.STRING)
    private TicketType type;

    private String code;
    private TicketStatusDTO status;
    private UserDTO assignee;
    private TicketPriority priority;
    private Long position;

}
