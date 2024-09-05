package com.yurjinia.project_structure.ticket.dto;

import lombok.Data;

@Data
public class UpdateTicketPositionRequest {
    String columnName;
    String ticketCode;
    int ticketPosition;
}
