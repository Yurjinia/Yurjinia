package com.yurjinia.project_structure.column.dto;

import com.yurjinia.project_structure.ticket.dto.TicketDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnDTO {

    @NotNull
    private String name;

    private long columnPosition;

    private List<TicketDTO> tickets;

}
