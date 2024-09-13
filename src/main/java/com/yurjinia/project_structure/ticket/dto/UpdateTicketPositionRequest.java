package com.yurjinia.project_structure.ticket.dto;

import lombok.Data;

@Data
public class UpdateTicketPositionRequest {
    private String columnName;
    private String ticketCode;
    private int ticketPosition;
}
