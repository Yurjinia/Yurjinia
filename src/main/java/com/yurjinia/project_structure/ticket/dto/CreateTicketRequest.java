package com.yurjinia.project_structure.ticket.dto;

import com.yurjinia.project_structure.ticket.entity.TicketType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter
public class CreateTicketRequest {

    private String name;

    @Enumerated(EnumType.STRING)
    private TicketType type;

}
