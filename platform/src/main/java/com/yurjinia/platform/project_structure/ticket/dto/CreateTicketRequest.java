package com.yurjinia.platform.project_structure.ticket.dto;

import com.yurjinia.platform.project_structure.ticket.entity.TicketType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTicketRequest {

    private String title;

    @Enumerated(EnumType.STRING)
    private TicketType type;

}
