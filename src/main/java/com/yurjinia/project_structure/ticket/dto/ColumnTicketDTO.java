package com.yurjinia.project_structure.ticket.dto;

import com.yurjinia.project_structure.ticket.entity.TicketPriority;
import com.yurjinia.project_structure.ticket.entity.TicketType;
import com.yurjinia.user.dto.UserDTO;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
