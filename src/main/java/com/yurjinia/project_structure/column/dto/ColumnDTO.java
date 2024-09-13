package com.yurjinia.project_structure.column.dto;

import com.yurjinia.project_structure.ticket.dto.TicketDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnDTO {

    @NotNull
    private String name;

    private long columnPosition;

    private List<TicketDTO> tickets;

}
