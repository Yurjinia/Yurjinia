package com.yurjinia.platform.project_structure.ticket.dto;

import lombok.Getter;

@Getter
public class UpdateTicketPositionRequest {
    private String columnName;
    private String ticketCode;
    private int ticketPosition;
}
