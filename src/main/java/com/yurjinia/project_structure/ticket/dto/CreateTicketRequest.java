package com.yurjinia.project_structure.ticket.dto;

import com.yurjinia.project_structure.ticket.entity.TicketType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
//TODO: remove @Setter
@Setter
public class CreateTicketRequest {

    private String title;

    @Enumerated(EnumType.STRING)
    private TicketType type;

}
