package com.yurjinia.project_structure.ticket.dto;

import com.yurjinia.project_structure.ticket.entity.TicketType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
public class UpdateTicketMetaDataRequest {

    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private TicketType type;

}
